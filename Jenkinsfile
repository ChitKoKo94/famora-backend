pipeline {
    agent any

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'amazon/aws-cli'

                }
                steps {
                    sh '''
                        mvn -v
                    '''
                }
            }
        }
    }
}