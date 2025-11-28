pipeline {
    agent any
     tools {
        maven "M2_HOME"
    }
    
    environment {
    
        DOCKERHUB_CRED = credentials('dockerhub')
    }
    stages {
        stage("Code Checkout") {
            steps {
                git branch: 'main',
                    url: 'https://github.com/yahyaelkaed/yahyaelkaed-SAE9-jenkins'
            }
        }
        stage('Code Test') {
            steps {
                sh "mvn test"
            }
        }
        stage('Code Build') {
            steps {
                sh "mvn package"
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t elkaedyahya/student ."
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                sh 'echo "$DOCKERHUB_CRED_PSW" | docker login -u "$DOCKERHUB_CRED_USR" --password-stdin'
                sh 'docker push $DOCKERHUB_CRED_USR/student'
            }
        }
    }
}
