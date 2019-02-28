bucketName="code-deploy.csye6225-spring2018-huangre.me"
resource="arn:aws:s3:::code-deploy.csye6225-spring2018-huangre.me/*"
resource2="arn:aws:s3:::web-app.csye6225-spring2018-huangre.me/*"

name=`aws cloudformation create-stack --stack-name $1 --template-body file://csye6225-cf-ci-cd.json --parameters ParameterKey=bucketName,ParameterValue=$bucketName ParameterKey=resource,ParameterValue=$resource ParameterKey=resource2,ParameterValue=$resource2 --capabilities CAPABILITY_NAMED_IAM`
aws cloudformation wait stack-create-complete --stack-name $1
flag=`aws cloudformation describe-stack-events --stack-name $1 --query 'StackEvents[0].ResourceStatus' --output text`
if [ "$flag" != "CREATE_COMPLETE" ];then
   echo "Fail to create $1 stack"
   exit 9
fi
   echo "Success to create $1 "
   exit 0
