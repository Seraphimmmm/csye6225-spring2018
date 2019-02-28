#!bin/bash
var=`aws cloudformation describe-stacks --stack-name $1`
result=$?
if [ "$result" != 0 ];then
   echo "Failure"
   exit 4
fi
aws s3 rm s3://web-app.csye6225-spring2018-huangre.me --recursive
name=`aws cloudformation delete-stack --stack-name $1`
aws cloudformation wait stack-delete-complete --stack-name $1 
   echo "Success to delete $1 stack"
