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
 stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.login=$SONAR_TOKEN \
                            -Dsonar.java.coveragePlugin=jacoco \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
            
            post {
                success {
                    echo "✅ SonarQube analysis submitted successfully"
                    script {
                        // Get the actual SonarQube URL from environment
                        def sonarUrl = env.SONAR_HOST_URL ?: 'http://localhost:9000'
                        echo "Check results at: ${sonarUrl}/dashboard?id=tn.esprit%3Astudent-management"
                    }
                }
                failure {
                    echo "❌ SonarQube analysis failed"
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t $DOCKERHUB_CRED_USR/student:latest ."
                }
            }
        }
        
        stage('Push to DockerHub') {
            steps {
                sh 'echo "$DOCKERHUB_CRED_PSW" | docker login -u "$DOCKERHUB_CRED_USR" --password-stdin'
                sh 'docker push $DOCKERHUB_CRED_USR/student:latest'
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "kubectl apply -f k8s/spring/spring-deployment.yaml -n devops"
                    sh "kubectl rollout status deployment/spring-app -n devops"
                }
            }
        }
    }
    
    post {
        always {
            sh "kubectl get pods -n devops"
        }
    }

}
