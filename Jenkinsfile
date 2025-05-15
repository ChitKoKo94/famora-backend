pipeline {
    agent any

    environment {
        SPRINGBOOT_APP_VERSION = "1.0.$BUILD_ID"
        AWS_DEFAULT_REGION = 'us-west-2'
        APP_IMAGE_NAME = 'famora-springboot-app'
        AWS_DOCKER_REGISTRY = '211125607599.dkr.ecr.us-west-2.amazonaws.com'
    }

    stages {
        stage('Compile') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21-alpine'
                    reuseNode true
                }
            }
            steps {
                sh '''
                    mvn -v
                    mvn -Dmaven.repo.local=$PWD/.m2 clean package
                '''
            }
        }
        stage('Build Docker Image') {
            agent {
                docker {
                    image 'amazon/aws-cli'
                    reuseNode true
                    args "-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint='' --network host"
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'my-aws', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
                    sh '''
                        amazon-linux-extras install docker
                        docker build -t $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$SPRINGBOOT_APP_VERSION .
                        aws ecr get-login-password | docker login --username AWS --password-stdin $AWS_DOCKER_REGISTRY
                        docker push $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$SPRINGBOOT_APP_VERSION
                    '''
                }
            }
        }
        stage('Deploy to ECS') {
            agent {
                docker {
                    image 'amazon/aws-cli'
                    reuseNode true
                    args "-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint='' --network host"
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'my-aws', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
                    sh '''
                        yum install jq -y
                        sed -i "s/#APP_VERSION#/$SPRINGBOOT_APP_VERSION/g" aws/task-definition.json
                        LATEST_TD_REVISION=$(aws ecs register-task-definition --cli-input-json file://aws/task-definition.json | jq '.taskDefinition.revision')
                        aws ecs update-service --cluster LearnJenkinsApp-CKK-Cluster-Prod \
                        --service Springboot-Prod-Service \
                        --task-definition Springboot-Task-Prod:$LATEST_TD_REVISION
                        aws ecs wait services-stable --cluster LearnJenkinsApp-CKK-Cluster-Prod --services Springboot-Prod-Service
                    '''
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }

}