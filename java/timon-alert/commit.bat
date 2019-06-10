echo off

set MSG=%1

echo %MSG%

IF [%MSG%] == [] (
    echo "Post MSG can be empty"
    GOTO:EOF
)

git status
git add .
git commit -a -m %MSG%
git push


