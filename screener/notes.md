DEV:

cljs screener:
$ boot dev

mailer service:
$ lein ring server-headless 4000

TODO:
[/] - configure auth permissions for aws-lambda fn exec
[/] - write code to take form input data and send email
[ ] - test code on heroku
[ ] - deploy dokku on digital ocean and configure ssh
[ ] - write code to collect data from input fields, ~ r/atom
[ ] - add sent-confirmation modal
