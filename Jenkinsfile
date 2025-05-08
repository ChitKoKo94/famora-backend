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
                    rm -rf aws aws-cli aws-cli-bin awscliv2.zip
                    apk update && apk add --no-cache curl unzip
                    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                    unzip -o awscliv2.zip
                    ./aws/install --install-dir $WORKSPACE/aws-cli --bin-dir $WORKSPACE/aws-cli-bin
                    export PATH="$WORKSPACE/aws-cli-bin:$PATH"
                    aws --version
                    mvn -v
                '''
            }
        }
    }
}