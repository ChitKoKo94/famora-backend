pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21-alpine'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                    mvn -v
                    apt-get update && apt-get install -y curl unzip
                    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                    unzip awscliv2.zip
                    ./aws/install
                    aws --version
                '''
            }
        }
    }
}