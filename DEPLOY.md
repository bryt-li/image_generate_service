# Deployment
```$xslt
Need port 61101
excute sql script in init_local_db
```

```$xslt
Need two dirs on server:
/logs/lollipop-gw
/data/lollipop-mq
```

## Modify startup.sh & shutdown.sh
modify to the versionyou're going to deploy
```
VERSION=0.0.1-SNAPSHOT
```

## Init deployment git repo
```
git init lollipop
```

## Allow push to non-bare repo
```
vi .git/config
```
add following lines
```
[receive]
    denyCurrentBranch = ignore
```

## Add automatic deployment script
```
touch .git/hooks/post-receive
chmod +x .git/hooks/post-receive
vi .git/hooks/post-receive
```
paste following to the `post-receive` file:
```
#!/bin/sh

set -x #echo on

set -e

echo "Auto deployment activated."

# Prepare build env
unset GIT_DIR
cd ..
git reset --hard

mvn clean
mvn package

./shutdown.sh

./startup.sh [develop/staging/product]
```

## Deploy
```
git remote add deploy-develop ops@xx.xx.xx.xx:/home/ops/deploy/lollipop/
```

To deploy:
```
git push deploy-[develop/staging/product] master
```