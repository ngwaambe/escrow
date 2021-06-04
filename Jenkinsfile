#!groovy

node {
    checkout scm
}

pipeline {
    agent any

    tools {
        jdk '11.0.11'
        maven 'latest' // latest
    }

    stages {
        stage('Checkout') {
           steps {
              git 'https://github.com/ngwaambe/escrow.git'
           }
        }
        stage("Build and package") {
            steps {
                sh 'mvn clean verify'
            }
        }
    }
}
