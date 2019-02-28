VPCcb="10.0.0.0/16"
routeDcb="0.0.0.0/0"
publicSubnetCb="10.0.0.0/24"
privateSubnetCb="10.0.1.0/24"
privateSubnetAz="us-east-1b"
privateSubnetCb2="10.0.2.0/24"
privateSubnetAz2="us-east-1a"
SGIp="tcp"
FromP="22"
FromP2="80"
FromP3="443"
FromP4="3306"
FromP5="8080"
CirIp="0.0.0.0/0"
name=`aws cloudformation create-stack --stack-name $1 --template-body file:///Users/huangrenteng/Desktop/assignment6/csye6225-cf-networking.json --parameters ParameterKey=VPCcb,ParameterValue=$VPCcb ParameterKey=routeDcb,ParameterValue=$routeDcb ParameterKey=publicSubnetCb,ParameterValue=$publicSubnetCb ParameterKey=privateSubnetCb,ParameterValue=$privateSubnetCb ParameterKey=privateSubnetAz,ParameterValue=$privateSubnetAz ParameterKey=privateSubnetCb2,ParameterValue=$privateSubnetCb2 ParameterKey=privateSubnetAz2,ParameterValue=$privateSubnetAz2 ParameterKey=SGIp,ParameterValue=$SGIp ParameterKey=FromP,ParameterValue=$FromP ParameterKey=FromP2,ParameterValue=$FromP2 ParameterKey=FromP3,ParameterValue=$FromP3 ParameterKey=FromP4,ParameterValue=$FromP4 ParameterKey=FromP5,ParameterValue=$FromP5 ParameterKey=CirIp,ParameterValue=$CirIp`
aws cloudformation wait stack-create-complete --stack-name $1
flag=`aws cloudformation describe-stack-events --stack-name $1 --query 'StackEvents[0].ResourceStatus' --output text`
if [ "$flag" != "CREATE_COMPLETE" ];then
   echo "Fail to create $1 stack"
   exit 9
fi
   echo "Success to create $1 "
   exit 0
