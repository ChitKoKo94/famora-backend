pipeline {
    agent any

    environment {
        APP_IMAGE_NAME = 'famora-springboot-app'
        AWS_DOCKER_REGISTRY = '211125607599.dkr.ecr.us-west-2.amazonaws.com'
    }

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21-alpine'
                }
            }
            steps {
                sh '''
                    mvn -v
                    mvn -Dmaven.repo.local=$PWD/.m2 clean package
                '''
            }
        }
        stage('Deploy') {
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
                        ls
                        #docker build -f target -t $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID .
                        #aws ecr get-login-password | docker login --username AWS --password-stdin $AWS_DOCKER_REGISTRY
                        #docker push $AWS_DOCKER_REGISTRY/$APP_IMAGE_NAME:$BUILD_ID
                        #yum install jq -y
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