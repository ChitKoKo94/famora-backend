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
    }

    post {
        always {
            cleanWs()
        }
    }
}