#!groovy

node {
    checkout scm
}

pipeline {
    agent any

    tools {
        jdk '11'
        maven 'default' // latest
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
