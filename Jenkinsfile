pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-west-2'
        APP_IMAGE_NAME = 'famora-springboot-app'
        AWS_DOCKER_REGISTRY = '211125607599.dkr.ecr.us-west-2.amazonaws.com'
    }

    stages {
    /*
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
                        yum install jq -y
                        docker build -t $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID .
                        aws ecr get-login-password | docker login --username AWS --password-stdin $AWS_DOCKER_REGISTRY
                        docker push $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID
                    '''
                }
            }
        }
    */
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
                        #amazon-linux-extras install docker
                        #yum install jq -y
                        #docker build -t $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID .
                        #aws ecr get-login-password | docker login --username AWS --password-stdin $AWS_DOCKER_REGISTRY
                        #docker push $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID

                        LATEST_TD_REVISION=$(aws ecs register-task-definition --cli-input-json file://aws/task-definition-prod.json | jq '.taskDefinition.revision')
                        aws ecs update-service --cluster LearnJenkinsApp-CKK-Cluster-Prod \
                        --service LearnJenkinsApp-Task-Prod-service-mf18smk5 \
                        --task-definition Springboot-Task-Prod:$LATEST_TD_REVISION
                        aws ecs wait services-stable --cluster LearnJenkinsApp-CKK-Cluster-Prod --services LearnJenkinsApp-Task-Prod-service-mf18smk5
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