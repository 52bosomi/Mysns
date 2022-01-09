# mysns
management our sns login


# github action sample

```yml
name: CI
on:
  create:
    tags:
      - 'v.*'
  workflow_dispatch:
jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      max-parallel: 1
      matrix:
        python-version: [3.7, 3.8]
    steps:
      - uses: actions/checkout@v2
#       - name: Install Python 3
#         uses: actions/setup-python@v1
#         with:
#           python-version: 3.6
      - name: Set up Python ${{ matrix.python-version }}
        uses: actions/setup-python@v2
        with:
          python-version: ${{ matrix.python-version }}
      - name: Install Dependencies
        run: |
          python -m pip install --upgrade pip
          pip install -r 03_web/anc.letter/requirements.txt
          
      - name: Run Tests
        env:
          CHECK_EXCUTEABLE: 1    
        run: |
          cd 03_web/anc.letter
          python app.py &
          
      - name: Self Killer
        run: |
          wget http://localhost:8080
          
  build-if-failed:
    runs-on: ubuntu-latest
    needs: [build]
    if: always() && (needs.build.result == 'failure')
    steps:
      - name: pre excute failed
        env:
          NEEDS_CONTEXT: ${{ toJSON(needs) }}
        run: |
          echo "$NEEDS_CONTEXT"
      - name: Slack Notification Failed
        uses: rtCamp/action-slack-notify@v2
        env:
          SLACK_COLOR: '#FF2D00' # or a specific color like 'good' or '#ff00ff'
#           SLACK_ICON: https://github.com/rtCamp.png?size=48 # later add favicon
          SLACK_TITLE: 'Preexcute python script was failed'
          SLACK_MESSAGE: ${{ github.event.head_commit.title }} ${{ github.event.head_commit.message }}
          SLACK_USERNAME: Smart-ANC-bot
          SLACK_WEBHOOK: ${{ secrets.SLACK_WEBHOOK }}

  # This workflow contains a single job called "build"
  deploy:
    needs: build
    # The type of runner that the job will run on
    runs-on: ubuntu-latest
    if: ${{ (needs.build.result != 'failure') }} # if not pass before step, skip this step
    # Steps represent a sequence of tasks that will be executed as part of the job
    steps:
      # Checks-out your repository under $GITHUB_WORKSPACE, so your job can access it
      - uses: actions/checkout@v2

      # Runs a single command using the runners shell
      - name: Set-Version
        run: |
          echo "RELEASE_VERSION=${GITHUB_REF#refs/*/v}" >> $GITHUB_ENV
          echo $GITHUB_ENV
      # pull buildx       
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v1
      
      # authorize ssl certificates for login
      - name: set cert
        run: bash cert.sh
      
      # login to harbor
      - name: Login to GitHub Container Registry
        uses: docker/login-action@v1
        with:
          registry: harbor.dndev.pw:4443
          username: ${{ secrets.HARBOR_BOT_ID }}
          password: ${{ secrets.HARBOR_BOT_PW }}
      # start build and push
      - name: Build the tagged Docker image
        run: docker build . --file Dockerfile --tag harbor.dndev.pw:4443/anc/letter:latest
      - name: Push the tagged Docker image
        run: docker push harbor.dndev.pw:4443/anc/letter:latest
      # for dev
#       - name: Build the tagged Docker image
#         run: docker build . --file Dockerfile --tag anc/letter:latest
#       - name: Push the tagged Docker image
#         run: docker push anc/letter:latest

  deploy-if-failed:
    runs-on: ubuntu-latest
    needs: [deploy]
    if: always() && (needs.deploy.result == 'failure')
    steps:
      - name: failed docker image deploy to harbor
        env:
          NEEDS_CONTEXT: ${{ toJSON(needs) }}
        run: |
          echo "$NEEDS_CONTEXT"
      - name: Slack Notification Failed
        uses: rtCamp/action-slack-notify@v2
        env:
          SLACK_COLOR: '#FF2D00' # or a specific color like 'good' or '#ff00ff'
#           SLACK_ICON: https://github.com/rtCamp.png?size=48 # later add favicon
          SLACK_TITLE: 'failed docker image deploy to harbor'
          SLACK_MESSAGE: ${{ github.event.head_commit.title }} ${{ github.event.head_commit.message }}
          SLACK_USERNAME: Smart-ANC-bot
          SLACK_WEBHOOK: ${{ secrets.SLACK_WEBHOOK }}
          
      
  # This workflow contains a single job called "build", ${{ env.RELEASE_VERSION }}
  apply:
    # The type of runner that the job will run on
    needs: deploy
    runs-on: ubuntu-latest

    # Steps represent a sequence of tasks that will be executed as part of the job
    steps:
      # Runs a single command using the runners shell
      - name: Webhook
        run: curl -s -d "payload={\"feel\":\"WTF\"}" "${{ secrets.WEB_HOOK_TARGET }}"
      
      - uses: actions/checkout@v2
      - name: Slack Notification
        uses: rtCamp/action-slack-notify@v2
        env:
          SLACK_COLOR: ${{ job.status }} # or a specific color like 'good' or '#ff00ff'
#           SLACK_ICON: https://github.com/rtCamp.png?size=48 # later add favicon
          SLACK_TITLE: 'deployed new version for anc letter project'
          SLACK_MESSAGE: ${{ github.event.head_commit.title }} ${{ github.event.head_commit.message }}
          SLACK_USERNAME: Smart-ANC-bot
          SLACK_WEBHOOK: ${{ secrets.SLACK_WEBHOOK }}
  
  apply-if-failed:
    runs-on: ubuntu-latest
    needs: [apply]
    if: always() && (needs.apply.result == 'failure')
    steps:
      - name: WEBHOOK apply was failed
        env:
          NEEDS_CONTEXT: ${{ toJSON(needs) }}
        run: |
          echo "$NEEDS_CONTEXT"
      - name: Slack Notification Failed
        uses: rtCamp/action-slack-notify@v2
        env:
          SLACK_COLOR: '#FF2D00' # or a specific color like 'good' or '#ff00ff'
#           SLACK_ICON: https://github.com/rtCamp.png?size=48 # later add favicon
          SLACK_TITLE: 'WEBHOOK apply was failed'
          SLACK_MESSAGE: ${{ github.event.head_commit.title }} ${{ github.event.head_commit.message }}
          SLACK_USERNAME: Smart-ANC-bot
          SLACK_WEBHOOK: ${{ secrets.SLACK_WEBHOOK }}

```