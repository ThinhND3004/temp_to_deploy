pipeline {
    agent any

    stages {
//         stage('Checkout') {
//             steps {
//                 git branch: 'main',
//                     url: 'https://github.com/ThinhND3004/temp_to_deploy.git'
//             }
//         }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                sh '''
                docker-compose down || true
                docker-compose up -d --build
                '''
            }
        }
    }
}
