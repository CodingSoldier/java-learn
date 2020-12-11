Linux内核是一个类Unix的内核。GNU创建操作系统的软件，GNU软件：cp命令、rm命令、Emacs、GCC、GDB等。
GNU项目+Linux（系统内核）= GNU/Linux 完整的操作系统，为了称呼方便，一般都称为Linux。

Linux发行版
  RHEL：性能稳定，老牌的linux发行版。收费的是RHEL
  Fedora: Red Hat 的社区免费后继版
  CentOS：算是RHEL的克隆版，免费。
  Deepin: 中国发行，对优秀的开源产品进行集成和配置，开发软件
  Debian: 算是迄今为止最遵循GNU规范的Linux系统
  Ubuntu: Debian的后继或者一个分支
#  01-linux发行版之间的关系.jpg

Centos7下载列表 http://isoredirect.centos.org/centos/7/isos/x86_64/
阿里云Centos下载节点 http://mirrors.aliyun.com/centos/7.9.2009/isos/x86_64/

virtualbox下载 https://www.virtualbox.org/wiki/Downloads


什么是tty？
  在linux中，TTY也许是跟终端有关系的最为混乱的术语
  TTY是TeleTYpe的一个缩写
  Teletypes、teletypewriters原来指的是电传打字机


[root@localhost ~]#
  root是当前用户的账号
  localhost是主机名
  ~ 当前所在目录，~表示当前用户的家目录（home directory）
  # 权限字符，表示超级用户。$ 表示普通用户

sudo su 切换用户

短参数以一个短横线开始
  command -a -p -c 多个短参数可以合并 command -apc

长参数以两个短横线开始，长参数不能像短参数那样合并
  command --parameter

可以组合使用长参数和短参数
  command -paTc --parameter1 --parameter2

短参数与长参数的赋值方式不一样
  短参数赋值使用空格 command -p 10
  长参数赋值使用等号 command --parameter=10


如何找到一个命令
  使用Tab键补全命令

Tab键可以补全命令，也可以补全文件名、路径名：按两次Tab键

使用 Ctrl + R 查找使用过的命令
history 列出之前使用过的所有命令。可以使用感叹号加序号再次执行历史明日，例如：!10

Ctrl + L 清屏，与clear一样
Ctrl + D 给终端传递EOF（End Of File，文件结束符）
Shift + PgUp 用于向上滚屏，与鼠标的滚轮向上滚屏是一个效果
Shift + PgDn 向下滚屏
Ctrl + A 光标跳到一行命令的开头，与Home键效果一样
Ctrl + E 光标跳到一行命令的开头，与End键效果一样
Ctrl + U 删除光标左侧字符，不包括光标字符
Ctrl + K 删除光标右侧字符，包括光标字符
Ctrl + W 删除光标左侧的一个“单词”，这里的单词是指两边用空格隔开的字符串
Ctrl + Y 粘贴之前用 Ctrl+U、Ctrl+K、Ctrl+U 删除的字符串

Linux中一切都是文件
Liunx中有且只有一个根目录，就是 / （斜杠），Linux中没有比根目录再高一级的目录了

Linux根目录下各目录的作用
bin
  binary的缩写，表示二进制文件，bin目录包含了被所有用户使用的可执行程序
boot
  启动的意思，包含了与启动密切相关的文件
dev
  device的缩写，表示设备。包含了外设，此目录的每个子目录对应一个外设
etc
  etc是法语et cetera的缩写，在英语中是and so on，表示“...等等”。
  etc目录包含了系统的配置文件
  按照Unix的说法，此目录存放了一堆零零碎碎的东西，就叫etc吧
home
  家目录，用户的私人目录，放置私人文件，类似Window中的“我的文档”
  root用户拥有所有权限，home目录下没有root目录
lib
  library的缩写，表示“库”，包含被程序所调用的库文件，例如：.so 结尾的文件
media
  表示“媒体”，可移动外设（USB，光盘）插入电脑时，通过media的子目录来访问外设中的内容
mnt
  mount的缩写，表示“挂载”，用于临时挂载一些装置
opt
  optional




















