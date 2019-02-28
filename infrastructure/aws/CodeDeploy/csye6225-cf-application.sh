
bucketName="web-app.csye6225-spring2018-huangre.me"
vpc=`aws ec2 describe-vpcs --query Vpcs[0].VpcId --output text`
privateSubnet1=`aws ec2 describe-route-tables --filters Name=tag-value,Values=myPrivateRouteTable --query RouteTables[0].Associations[0].SubnetId --output text`
privateSubnet2=`aws ec2 describe-route-tables --filters Name=tag-value,Values=myPrivateRouteTable --query RouteTables[0].Associations[1].SubnetId --output text`
PublicSubnet=`aws ec2 describe-route-tables --filters Name=tag-value,Values=myPublicRouteTable --query RouteTables[0].Associations[0].SubnetId --output text`
PublicSubnet2=`aws ec2 describe-route-tables --filters Name=tag-value,Values=myPublicRouteTable --query RouteTables[0].Associations[1].SubnetId --output text`
ImageId="ami-66506c1c"
Az="us-east-1a"
InstanceType="t2.micro"
DBUnit="5"
DBTableName="csye6225"
RDSstorage="100"
MySQLVersion="5.6.37"
RDSDbInstanceClass="db.t2.medium"
RDSInstanceI="csye6225-spring2018"
masterUser="csye6225master"
masterPassword="csye6225password"
InstanceSecurityGroup=`aws ec2 describe-security-groups --filter Name=tag-value,Values=csye6225-webapp --query SecurityGroups[0].GroupId --output text`
RdsSecurityGroup=`aws ec2 describe-security-groups --filter Name=tag-value,Values=csye6225-rds --query SecurityGroups[0].GroupId --output text`

name=`aws cloudformation create-stack --stack-name $1 --template-body file://csye6225-cf-application.json --parameters ParameterKey=bucketName,ParameterValue=$bucketName ParameterKey=vpc,ParameterValue=$vpc ParameterKey=privateSubnet,ParameterValue=$privateSubnet1 ParameterKey=privateSubnet2,ParameterValue=$privateSubnet2 ParameterKey=ImageId,ParameterValue=$ImageId ParameterKey=Az,ParameterValue=$Az ParameterKey=InstanceType,ParameterValue=$InstanceType ParameterKey=DBUnit,ParameterValue=$DBUnit ParameterKey=DBTableName,ParameterValue=$DBTableName ParameterKey=RDSstorage,ParameterValue=$RDSstorage ParameterKey=MySQLVersion,ParameterValue=$MySQLVersion ParameterKey=RDSDbInstanceClass,ParameterValue=$RDSDbInstanceClass ParameterKey=RDSInstanceI,ParameterValue=$RDSInstanceI ParameterKey=masterUser,ParameterValue=$masterUser ParameterKey=masterPassword,ParameterValue=$masterPassword ParameterKey=InstanceSecurityGroup,ParameterValue=$InstanceSecurityGroup ParameterKey=RdsSecurityGroup,ParameterValue=$RdsSecurityGroup ParameterKey=PublicSubnet,ParameterValue=$PublicSubnet ParameterKey=PublicSubnet2,ParameterValue=$PublicSubnet2 --capabilities CAPABILITY_NAMED_IAM`

aws cloudformation wait stack-create-complete --stack-name $1
flag=`aws cloudformation describe-stack-events --stack-name $1 --query 'StackEvents[0].ResourceStatus' --output text`
if [ "$flag" != "CREATE_COMPLETE" ];then
   echo "Fail to create $1 stack"
   exit 9
fi
   echo "Success to create $1 "
   exit 0

