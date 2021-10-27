# CastexSki - External services

The CastexSki external services.

## Requirements

### Mono

To install Mono:

```bash
sudo apt install mono-complete
```

### JFrog CLI

To install JFrog CLI:

```bash
wget -qO - https://releases.jfrog.io/artifactory/api/gpg/key/public | sudo apt-key add -;
echo "deb https://releases.jfrog.io/artifactory/jfrog-debs xenial contrib" | sudo tee -a /etc/apt/sources.list;
sudo apt update;
sudo apt install -y jfrog-cli;
```

To configure JFrog CLI:

```bash
jfrog config import "$(< jfrogCli_IsaDevops.config)"
```
