pipeline {
    agent any

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
                sh '''
                    amazon-linux-extras install docker
                    ls -la
                    docker images ls
                '''
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}