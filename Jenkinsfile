pipeline {
    agent any
    triggers {
        upstream(
            upstreamProjects: 'isa-devops-2021-team-f-client,isa-devops-2021-team-f-external,isa-devops-2021-team-f-server',
            threshold: hudson.model.Result.SUCCESS
        )
    }
    stages {
        stage('Update') {
            when {
                branch 'master'
            }
            steps {
                sshagent (credentials: ['github-private-ssh-key']) {
                    // Configure the user.
                    sh 'git config user.email "isa.devops.2021.team.f@gmail.com"'
                    sh 'git config user.name "IsaDevOps2021TeamF"'
                    sh 'git config core.sshCommand "ssh -v -o StrictHostKeyChecking=no"'

                    // Update the submodules.
                    sh 'git checkout master'
                    sh 'git submodule update --init --remote'

                    // Commit the changes.
                    sh 'git add -A'
                    sh 'git commit -m "Update the submodules." || echo "No changes to commit."'
                    sh 'git pull origin master'
                    sh 'git push origin master'
                }
            }
        }
    }
}
