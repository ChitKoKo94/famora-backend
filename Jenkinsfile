pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21-alpine'
        }
    }

    environment {
        PATH='${env.WORKSPACE}/aws-cli-bin:${env.PATH}'
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                    mvn -v
                    apk update && apk add --no-cache curl unzip
                    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                    unzip -o awscliv2.zip
                    ./aws/install --install-dir $WORKSPACE/aws-cli --bin-dir $WORKSPACE/aws-cli-bin
                    aws --version
                '''
            }
        }
    }
}