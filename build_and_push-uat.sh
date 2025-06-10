#!/bin/bash

AWS_REGION="ap-south-1"
ECR_URL="537124947008.dkr.ecr.ap-south-1.amazonaws.com"
ECR_REPOSITORY="gf-dashboard-service"
DOCKER_IMAGE_TAG="latest"
ROLE_ARN="arn:aws:iam::537124947008:role/gf-uat-bitbucket-role"
SESSION_NAME="ecr-push-session-fr"
Enviornment="uat"


# Get temporary credentials by assuming the IAM role
CREDS=$(aws sts assume-role --role-arn $ROLE_ARN --role-session-name $SESSION_NAME)

echo "Logging in to AWS ECR..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_URL

echo "Building Docker image for ${SERVICE_NAME}..."
#docker build -f Dockerfile -t $ECR_URL/$ECR_REPOSITORY:$DOCKER_IMAGE_TAG .
docker build --build-arg SPRING_PROFILE=$Enviornment -t $ECR_URL/$ECR_REPOSITORY:$DOCKER_IMAGE_TAG .

echo "Pushing Docker image to ECR Repo ${ECR_REPOSITORY}/${ECR_URL}/${DOCKER_IMAGE_TAG}..."
docker push $ECR_URL/$ECR_REPOSITORY:$DOCKER_IMAGE_TAG