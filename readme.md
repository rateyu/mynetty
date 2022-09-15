## 已存在的git仓库推送
git remote add origin git@github.com:rateyu/mynetty.git
git branch -M main
git push -u origin main

## 同时两个仓库提交代码
进入.git/config目录
[remote "origin"]
url = git@gitee.com:rateyu/mynety.git
url = git@github.com:rateyu/mynetty.git
fetch = +refs/heads/*:refs/remotes/origin/*