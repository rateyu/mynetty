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
[branch "main"]
remote = origin
merge = refs/heads/main

## git merger test
快速合并
分支从主干开出来进行开发，但是主干没有变化，此种情况可快速合并
合并完 push完后，dev和main 指针相同

测试回滚

