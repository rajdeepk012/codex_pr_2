#!/bin/bash

SERVICE_NAME="gf-dev-dashboard-service"
FAMILY="gf-dev-dashboard-service-tf"
Application_Name="AppECS-gf-dev-ecs-cluster-gf-dev-dashboard-service"
Deployment_Group="DgpECS-gf-dev-ecs-cluster-gf-dev-dashboard-service"
REGION="ap-south-1"
CLUSTER="gf-dev-ecs-cluster"
ROLE_ARN="arn:aws:iam::869935070506:role/gf-dev-bitbucket-role"
SESSION_NAME="ecs-session"
Container_Name="gf-dashboard-service"


# Get temporary credentials by assuming the IAM role
ROLE_CREDENTIALS=$(aws sts assume-role --role-arn $ROLE_ARN --role-session-name $SESSION_NAME)
export AWS_ACCESS_KEY_ID=$(echo $ROLE_CREDENTIALS | jq -r .Credentials.AccessKeyId)
export AWS_SECRET_ACCESS_KEY=$(echo $ROLE_CREDENTIALS | jq -r .Credentials.SecretAccessKey)
export AWS_SESSION_TOKEN=$(echo $ROLE_CREDENTIALS | jq -r .Credentials.SessionToken)
export AWS_REGION=ap-south-1



TASK_ARN_DEFINITION=`aws ecs describe-task-definition --task-definition ${FAMILY}  --region ${REGION} | jq .taskDefinition.taskDefinitionArn -r`

REVISION=`aws ecs describe-task-definition --task-definition ${FAMILY} --region ${REGION} | jq .taskDefinition.revision`

OLD_TASK_ID=`aws ecs list-tasks --cluster ${CLUSTER} --region ${REGION} --query "taskArns" --output text | awk -F ":"  '{print $6}'  | awk -F "/" '{print $3}'`

> appsec.yaml

echo "---
version: 0.0
Resources:
  - TargetService:
      Type: AWS::ECS::Service
      Properties:
        TaskDefinition: "${TASK_ARN_DEFINITION}"
        LoadBalancerInfo:
          ContainerName: "${Container_Name}"
          ContainerPort: 8085
" > appsec.yaml

cat appsec.yaml

aws ecs register-task-definition --family ${FAMILY} --cli-input-json file://task.json --region ${REGION}

DESIRED_COUNT=`aws ecs describe-services --service ${SERVICE_NAME}  --cluster ${CLUSTER} --region ${REGION} | jq .services[].desiredCount`

#aws deploy create-deployment --application-name ${Application_Name} --deployment-group-name ${Deployment_Group} --revision "revisionType=AppSpecContent,appSpecContent={content='$(cat appsec.yaml)'}" --deployment-config-name CodeDeployDefault.ECSAllAtOnce --region ${REGION} --description "Blue-Green Deployment for ECS Service"

aws ecs deploy --service ${SERVICE_NAME} --task-definition task.json --codedeploy-appspec appsec.yaml  --cluster ${CLUSTER} --region ${REGION} --codedeploy-application ${Application_Name} --codedeploy-deployment-group ${Deployment_Group}