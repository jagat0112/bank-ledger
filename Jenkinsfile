pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "jgtpradhan/bank-ledger:v1"
        APP_SERVER = "ubuntu@18.188.10.12"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/jagat0112/bank-ledger.git', credentialsId: 'github-credentials'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build --platform linux/amd64 -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['app-ec2-ssh-key']) {
                    withCredentials([string(credentialsId: 'rds-password', variable: 'DB_PASS')]) {
                        sh """
                        ssh -o StrictHostKeyChecking=no ${APP_SERVER} '
                            docker pull ${DOCKER_IMAGE} &&
                            docker stop bank-ledger || true &&
                            docker rm bank-ledger || true &&
                            docker run -d --name bank-ledger -p 8080:8080 \
                              -e SPRING_DATASOURCE_URL=jdbc:mysql://ledger-db.cnsy8q2mwd3t.us-east-2.rds.amazonaws.com:3306/ledgerdb \
                              -e SPRING_DATASOURCE_USERNAME=admin \
                              -e SPRING_DATASOURCE_PASSWORD=${DB_PASS} \
                              ${DOCKER_IMAGE}
                        '
                        """
                    }
                }
            }
        }
    }
}