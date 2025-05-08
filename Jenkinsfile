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
                    # Clean up previous installations
                    rm -rf aws aws-cli aws-cli-bin awscliv2.zip
                    apk update && apk add --no-cache curl unzip

                    # Download and install AWS CLI
                    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                    unzip -o awscliv2.zip
                    ./aws/install --install-dir $WORKSPACE/aws-cli --bin-dir $WORKSPACE/aws-cli-bin

                    # Verify installation
                    echo "Listing AWS CLI directory:"
                    ls -l $WORKSPACE/aws-cli-bin
                    echo "PATH: $PATH"

                    # Add AWS CLI to the PATH explicitly
                    export PATH="$WORKSPACE/aws-cli-bin:$PATH"
                    aws --version
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