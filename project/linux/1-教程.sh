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
  optional application software package 的缩写，表示“可选的应用软件包”。用于安装多数第三方软件和插件。
root
  超级用户root的家目录，一般用户的家目录位于/home目录下，root用户是个例外
sbin
  英语system binary的缩写，表示“系统二进制文件”，比bin目录多了一个前缀system，sbin目录包含系统级的重要可执行程序
srv
  英语service的缩写，表示“服务”，包含一些网络服务启动之后所需要用到的数据
tmp
  temporary的缩写，表示“临时的”
usr
  Unix Software Resource的缩写，表示“Unix操作系统软件资源”，类似etc目录，也是历史遗留的命名
  是最庞大的目录之一，类似window中的C:\Windows和C:\Program Files两个文件的集合
  usr目录安装了大部分用户要调用的程序
var
  variable，表示“动态的、可变的”，通常包含程序的数据，比如log（日志）文件

以上目录列表的形式在类Unix的操作系统里是类似的，苹果的maxOS目录结构也是从根目录/开始的

pwd 是 Print Working Directory 打印当前工作目录 的缩写

which 命令：获取命令的可执行文件的位置，用于显示一个命令对应的可执行程序的位置
  在Linux中，每一条命令其实对应了一个可执行程序，在终端输入命令，按回车键，就是执行了对应的程序
  which命令接收一个参数，是你想知道可执行程序在哪里的命令
  例如：which pwd  显示pwd在/usr/bin/pwd

ls 命令：list的缩写，用于列出文件和目录
  一般来说
    蓝色 表示 目录
    绿色 表示 可执行文件
    红色 表示 压缩文件
    浅蓝色 表示 链接文件
    灰色 表示 其他文件
ls 命令的参数
  -a 显示所有文件和目录，包括隐藏文件
    以点（.）开头的文件是隐藏文件。但是.和..这两个的含义比较特殊
    .（一个点）表示当前目录，..（两个点）表示上一级目录
  -A 与-a基本相同，但是不列出.、..这两个文件
  -l 列出详细信息
    总用量 92  92表示当前目录第一层级所有文件的总大小，单位是KB
    drwxr-xr-x.  13 root root   155 11月 17 2019 usr
      drwxr-xr-x. 文件权限
      13 链接数目
      root 文件的所有者名称
      root 文件所在的群组
      155 文件大小，单位是byte（字节）
      11月 17 2019 最近一次的修改时间
      usr 文件名/目录
  -h h是humain readable的缩写，表示“适合人类阅读的”
    ll -h 或者 ls -lh
  -t time的缩写，按最近一次修改时间排序
    ll -t

cd 命令，change directory 的缩写，表示“切换目录”
  cd ~ 与 cd 命令能回到家目录

du 命令，相比于 ls -l 命令，du命令统计的才是真正的文件大小 
  du 深入遍历每个目录的子目录，统计所有文件的大小
  disk usage的缩写，表示“磁盘使用/占用”
  du命令默认只列出目录的大小，加上 -a显示目录、文件大小

cat 命令，concatenate的缩写，表示“连接/串联”
  可以一次性在终端显示文件的所有内容

less 分页显示文件内容，一页一页地显示内容
  more 命令与 less 类似，但没有less那么强大
  空格键：往下翻一个屏幕的内容，与PageDown效果一样
  回车键：读取下一行，与向下箭头一样
  d: 往下翻半个屏幕内容
  b: 往上翻一个屏幕，与PageUp效果一样
  y: 往上翻一行，与向上箭头一样
  u: 往上翻半个屏幕内容
  d: 停止读取文件，终止less命令 
  =：显示当前在文件中的位置，按 enter 取消
  h: 显示帮助文档，q退出帮助文档
  /: 搜索模式，按enter确定，按n跳到下一个，shit+n跳到上一个

head 显示文件开头
tail 显示文件结尾
  默认情况下显示文件10行
  -n 指定显示的行数
  -f 实时追踪文件的更新

touch 创建一个空白文件

mkdir 创建一个目录，创建一个目录
  -p: 递归创建目录结构  mkdir -p one/two/three

cp 拷贝文件或目录
  cp 已经存在的文件 新文件
  -r或者-R：拷贝目录，r表示recursive（递归），目录中的子目录，文件都会被拷贝

mv 移动文件（目录）/重命名文件（目录）

rm 删除文件目录
  -i: inform的缩写，表示“通知”，删除每个文件都向用户询问是否删除
  -f: force的缩写，表示“强制”，强制删除文件
  -r: 递归删除

ln link的缩写，表示“链接”，用于文件之间创建链接
  linux有两种连接类型 
    Physical link：物理链接或者硬链接
    Symbolic link: 符号链接或者软连接
  硬链接：
    使链接的两个文件共享同样的文件内容，就是同样的inode
    硬链接只能创建指向文件的硬链接，不能创建指向目录的
    ln 文件 硬链接名
  软连接：可以指向文件和目录，软链接更常用
    创建软连接需要加上 -s 参数，s是symbolic（符号）的缩写
    ln -s 文件 软连接名

sudo Substitute User DO 的缩写，substitute是替换的意思。以root身份运行命令
Liunx可以创建多个用户，这些用户被划分到不同的群组里面
root比较特殊，是超级用户


sudo su  一直成为root用户，输入当前用户的密码
su - 切换到root用户，需要输入root用户的密码
  su - 切换到root的家目录
  su 切换到之前用户的家目录

useradd testuser  创建用户testuser
ls /home 查看用户的家目录
passwd testuser  给已创建的用户testuser设置密码
usermod --help  修改用户这个命令的相关参数
userdel testuser  删除用户testuser，不会删除家目录
  -r 或者 --remove 删除家目录

如果不设置用户群组，默认会创建一个和它用户名一样的群组

groupadd 添加一个新的群组
  groupadd friend
  groupadd happy
  groupadd funny

usermod 是user、modify的缩写，修改用户账号
  -l:重命名用户
  -g：修改用户所在群组
    usermod -g friend testuser
  -G: 将用户添加到多个群组
    usermod -G friend,happy,funny testuser
  使用usermod -g/-G 会将用户原来的群组剔除
  -aG: a是append的缩写，这样不会剔除原来的群组

groups 查询用户所在的群组
  groups testuser

groupdel 删除群组
  groupdel happy

chown change和owner的缩写，改变文件所有者（文件群组不变）
  chown 用户名 文件名     //改变文件所有者（文件群组不变）
  chown 用户名:群组名 文件名    //修改文件所有者和群组
  -R：只能用R，小写的r不起作用。     目录与其子级都会被修改


chgrp change和group的缩写，改变文件的群组
  chgrp 群组名 文件名

ll 命令显示权限
  第一个字母，d 表示目录，l 表示链接
  最后一个点表示SELinux的安全标签
  一共有三组
      d    rwx     rwx      rwx
    属性 所有者 群组用户 其他用户

chmod change和mode的缩写，修改文件访问权限
  linux权限与对应的数字
    r  4
    w  2
    x  1
    综合来看，744、755、766比较常用
  使用字母分配权限
    chmod u+rx 文件名  // 文件所有者增加读、运行权限 
    chmod g+r 文件名   // 文件群组用户增加读权限
    chmod g+r o-r 文件名   // 文件群组用户增加读权限，其他用户组移除读权限
    chmod go-r 文件名   // 文件群组用户、其他用户组移除读权限
    chmod +x 文件名  // 文件的所有用户增加运行权限
  -R：递归修改权限


Nano文本编辑器
  linux终端比较著名的文本编辑器有：Nano、Vim、Emacs
  输入nano ，回车，即可打开nano输入内容
  打开nano后，底下会显示帮助文档。^表示Ctrl键，后面的字母虽然是大写，但却是对应小写的按键
  ctrl + g  显示帮助文档
  ctrl + x  退出帮助文档
  nano 文件名  // 创建文件并编辑

.bashrc 是Bash这个shell程序的配置文件，可通过此文件配置终端
  Bash使用最常用的shell程序，CentOS和大部分常见的Linux发行版默认shell是Bash

/etc/profile是非图形界面终端的配置文件
  profile文件会调用.bashrc文件，profile会在profile本身配置的基础三加上.bashrc的配置
  使用source命令使改动立即生效
    source /etc/profile

Linux软件包，window中的软件在Linux中叫做软件包
  rpm是Red Hat Package Manager的缩写，表示“红帽软件包管理器”
  Debian一族的软件包后缀是.deb为后缀
  几乎所有的.rpm软件包放在相同的地方，称为软件仓库repository


软件仓库文件是  /etc/yum.repos.d/CentOS-Base.repo
CentOS官方的源列表 https://www.centos.org/download/mirrors/

修改CentOS默认yum源为mirrors.aliyun.com
  1、备份系统自带yum源配置文件/etc/yum.repos.d/CentOS-Base.repo
    mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
  2、下载ailiyun的yum源配置文件到/etc/yum.repos.d/
    wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
  3、运行yum makecache生成缓存
    yum makecache
  4、这时候再更新系统就会看到以下mirrors.aliyun.com信息。这一步可不执行
    yum -y update

yum update/upgrade  更新软件包。update删除旧软件包（首选），upgrade不删除旧软件包
yum search  搜索软件包
yum install  安装软件包
yum remove 或者 yum autoremove   删除软件包

rpm -i *.rpm    安装软件包
rpm -e 包名     卸载软件包

RTFM  阅读那该死的手册

man命令，显示使用手册，是manual的缩写。man命令后接命令、函数
  mandb  更新手册
  man ls  查看ls命令手册
  按 q 退出手册页
  SYNOPSIS区域中
    粗体文字表示要原封不动地输入
    ...省略号表示可以有多个此类内容
    [OPTION]：中括号[]表示可选的，OPTION表示选项参数
      选项参数在DESCRIPTION区域中
    下划线文字表示要用实际内容替换

apropos 命令：查找命令。用法：apropos命令后接一个关键字
  运行 apropos sound ，列出所有使用手册中有sound这个关键字的命令，左侧是命令的名字，右侧是命令手册中出现关键字的句子

命令 -h/--help  显示帮助文档

whatis 命令，是man的精简版，显示手册的开头部分

locate命令，快速查找。搜索包含关键字的所有文件和目录
  安装
    1、yum install mlocate
    2、updatedb  //更新后台数据库
    3、locate inittab
  locate consul_1.4.4  查找包含consul_1.4.4文件名的文件
  刚创建的文件，locate查找不到
  locate不会搜索硬盘，而是在文件数据库中查找。Linux每天更新一次文件数据库，可用 updatedb 立即更新数据库
  03-locate命令原理.jpg 

find 查找命令，遍历硬盘 
  find 何处 何物 做什么
  find /var/log -name "syslog"  根据文件名查找，可以使用通配符，"*syslog*"
  find /var -size +10M    查找大于10M的文件，-10k是查找小于10k的文件
    G M k  k是小写
  -type d //只查找目录类型。-type f  //只查找文件类型
    find / -name boot -type d
  find /tmp -name "systemd*" -exec chmod 777 {} \;    查找/tmp目录下名称符合systemd*的文件，并修改这些文件的权限为777

find / -name "XXX*"     全局查找文件，*是通配符
find / -iname "XXX*"    iname忽略大小写

# 查找包含nginx字符串的文件
grep "nginx" XXX*     XXX*是文件前缀

grep 星期一  文件      在文件中查找星期一

# sed流编辑器
# s//将cron_date.txt中的日替换为天，仅在终端中暂时替换结果，不真正替换
sed 's/日/天/' cron_date.txt

# -i真正替换文件
sed -i 's/日/天/' cron_date.txt   

grep查找关键字，并显示关键字所在的行
grep text file   // text代表要搜索的文本，file代表供搜索的文件
  grep path /etc/profile
  -i  忽略大小写  grep -i path /etc/profile
  -n  显示文本所在的行号
  -E  使用正则表达式，CentOS默认是启用正则表达式的  grep -E ^path /etc/profile

sort 排序命令
  sort sortfile.txt    //排序sortfile.txt中的内容然后输出到屏幕，不区分大小写
  -o  将排序后的内容写入新文件
    sort -o new_file.txt sortfile.txt

wc命令，文件统计。统计单词数、行数、字符数，字节数
  wc显示3个数字。行数  单词数  字节数

cut命令，剪切文件内容

>  将标准输出重定向到新文件中，如果文件不存在就新建一个文件，如果文件存在则覆盖内容
>> 标准输出重定向到文件末尾，相当于追加
黑洞文件 /dev/null，发送到此文件的数据都会被作废



stdin    从键盘向终端输入数据，也就是标准输入，standard input的缩写，文件描述符为0
stdout   终端输出信息（不包括错误信息），文件描述符为1
stderr   终端输出的错误信息，standard error的缩写，标准错误输出独立于标准输出
         且标准输出和标准错误输出可以分别重定向，标准错误输出的文件描述符为2

cat not-exit-file.txt > my.log
  > 不会将标准错误输出重定向到my.log，my.log中没有内容

cat not-exit-file.txt > my.log 2> stderr.log
  标准输出重定向到my.log
  标准错误输出重定向到stderr.log，2是标准错误输出的文件描述符

2>    标准错误输出重定向 
2>>   标准错误输出重定向（追加）
2>&1  标准输出与标准错误输出重定向到同一个文件 
  cat not-exit-file.txt > my.log 2>&1
  cat not-exit-file.txt >> my.log 2>&1  //追加

# <   符号用于指定文件的输入
# <<  将键盘的输入重定向到某个命令的输入
# << END   接收键盘输入
#   sort -n << END   按END退出

# wc -m << END
# > How many characters are there in this sentence ?
# > END

| 管道符号


系统监控
  w命令、uptime命令、tload命令

ps命令，Process Status（进程状态），ps命令用于显示系统进程
  -ef  列出所有用户在所有终端的所有进程
  -H   进程之间有缩进层级
  -u 用户名   列出用户运行的进程
  -aux   通过CPU和内存使用来过滤进程，可通过 --sort 参数排序
    ps -aux --sort -pcpu  按CPU使用率排序
    ps -aux --sort -pmem  按内存使用率排序
    ps -aux --sort -pcpu,+pmem | head    将CPU、内存参数合并到一起，并通过管道显示前10个结果
  -axjf和pstree效果类似，都是以树形结构显示进程

top命令，动态显示进程状态
  按 q 退出
  按 u 可以显示指定用户启动的进程

CentOS7 安装系统监控软件 glances iftop htop
yum install epel* -y
yum install python-pip python-devel -y
yum install glances -y

ctrl + c  停止终端正在运行的进程
终端拷贝粘贴使用 ctrl + shift + c 和 ctrl + shift + v 

kill 结束一个进程，kill后面接PID

killall 结束多个进程，killall命令后接程序名，而不是PID

halt    关闭系统
reboot  重启系统
halt 和 reboot 这两个命令都使用了shutdown


&符号和nohup命令：后台进程

后台运行一个进程有几种方法
  1、在运行命令后面加上&符号，例如：find / -name "*log" &
    但是进程的输出还是会在终端输出，可以配合重定向使用
      find / -name "*log" > output_find &   但是这样标准错误输出会输出到屏幕
      find / -name "*log" > output_find 2>&1 &   标准输出、标准错误输出都重定向到一个文件。
    使用&的后台进程与终端香关联，终端关闭后，后台进程也会被关闭
  2、使用nohup使进程和终端分离
    当用户注销（logout）或者网络断开时，终端收到HUP（挂断，hangup的缩写）信号，从而关闭所有子进程。
    可以使用nohup命令使得进程不受HUP信号影响
    nohup find / -name "*log" > output_find 2>&1 &

Ctrl + z 转到后台，并暂停运行
bg background的缩写，表示“后台”。假如命令已经在后台并且暂停着，bg命令会将其状态改为运行
  如果不加任何参数，bg命令默认作用于最后一个后台进程。也就是刚才被Ctrl+z暂停的进程
  如果后面加了 %1，%2 则作用于指定标号的进程
    按下Ctrl+z后出现 [1]+  Stopped top  中括号中的1就是标号

Linux有5种常见的进程状态
  R 正在运行或者在运行队列中等待
  S 中断、休眠中
  D 不可中断，即进程没响应中断信号
  Z 僵死，进程终止，进程描述符依旧存在
  T 停止，进程收到SIGSTOP、SIGSTP、SIGTIN、SIGTOU等停止信号后停止运行

jobs命令，显示当前终端的后台进程

tar命令、gzip和bzip2命令的使用
  1、用tar将多个文件归档为一个总的文件，称为archive
  2、用gzip或者bzip2将archive压缩为更小的文件
    04-tar和gzip或bzip2.jpg
  c：create的缩写，表示“创建”
  v：verbose的缩写，表示“冗余”，会显示操作细节
  f：file的缩写，表示“文件”，指定归档文件
tar -cvf 归档后的文件ming.tar 归档目录
tar -cvf 归档后的文件ming.tar 文件1、文件2、文件3
  -rvf：追加文件到tar中
  -cvf：解压

gzip、bzip2压缩归档，只需要改后缀名
  .tar.gz   使用gzip压缩后的文件名
    gzip 01.tar  命令执行后文件名变成了 01.tar.gz
  .tar.bz2  使用bzip2压缩压缩后的文件名

gunzip、bunzip2命令解压

用tar命令同时完成归档和压缩操作
  -zcvf  用过gzip压缩归档
    tar -zcvf test.tar.gz .
  -zxvf
    解压gzip文件

  -jcvf  用bzip2压缩归档


yum install zip unzip

zip -r test.zip .
  -r 递归压缩，一定要加，不然压缩的就是空文件夹


编译安装的大致步骤
  下载源代码 解压压缩包 配置 编译 安装
 
安装软件
  1、使用yum搜索，有软件则用yum安装
  2、去软件的官网找后缀为 .rpm 安装包，Debian一族的后缀是 .deb
    yum会自动安装依赖，rpm安装方式要手动安装依赖
  3、编译源码安装

alien（外星人）可以实现rpm与deb的相互转换，但是不能保证100%顺利安装
    yum install alien 

编译就是将程序的源代码换成可执行文件的过程
  README文件一般会说明详细的安装步骤

编译安装 htop
https://github.com/htop-dev/htop/releases/tag/2.2.0   下载地址
tar zxvf htop-2.2.0.tar.gz     解压
cd htop-2.2.0/ 
cat README   阅读README
./autogen.sh  按照README提示，先执行./autogen.sh
  提示缺少依赖autoreconf，安装依赖
    yum install autoconfig automake libtool
    autoreconf -h
./configure --prefix=/XXX/path  按照README提示，执行 --prefix=/XXX/path
  文档提示默认将安装到 /usr/local 目录，最好在configure这一步修改安装位置
  ./configure --prefix=/some/path
  提示You may want to use --disable-unicode or install libncursesw.
    yum install ncurses-devel -y
  重新运行 --prefix=/XXX/path 直至没有错误
make 按照README提示，运行 make
make install  按照提示，运行 make install
htop 安装完成，运行htop


https://blog.csdn.net/zhaihaifei/article/details/54617516
05-net-tools与iproute2比较.png


SSH结合使用非对称加密、对称加密两种方式
  首先使用非对称加密，安全地传输对称加密的秘钥
  然后使用堆成加密的秘钥作为加密、解密的手段

OpenSSH是SSH协议的免费开源实现
OpenSSH分为客户端和服务端
  客户端 openssh-client
    yum install openssh-clients
  服务端 openssh-server
    yum install openssh-server
  安装完成后，会开启守护进程sshd
  手动开启sshd，安装后自动开启了
    systemctl start sshd
  停止
    systemctl stop sshd

服务端安装openssh-server，默认已经安装了
  yum install openssh-server

Linux客户端安装SSH工具
  yum install openssh-clients

Linux客户端使用SSH登陆Liunx服务端
  ssh root@192.168.1.198  使用root用户登录，之后要确认，输入密码

ssh的config文件可以配置SSH，方便批量管理多个SSH连接
全局的config文件有两个
  客户端配置  /etc/ssh/ssh_config
  服务端配置  /etc/ssh/sshd_config
可使用man命令查看config文件的使用
  man ssh_config
  man sshd_config

每个用户的客户端的config配置文件地址，服务端没有
  ~/.ssh/config  

客户端config文件常用的配置参数，一般修改客户端
  Host       别名
  HostName   远程主机名或者IP地址
  Port       连接到远程主机的端口
  User       用户名

服务端config文件常用配置参数
  Port                        sshd服务端口号，默认是22
PermitRootLogin  yes        是否允许以root用户身份登录，默认可以
PasswordAuthentication yes  是否允许密码登陆，默认可以
PubkeyAuthentication yes    是否允许公钥验证登陆，默认可以
  PermitEmptyPasswords     是否允许空密码登陆，默认不可以
修改config文件后需要重启sshd
  systemctl restart sshd

基于秘钥的验证
  客户机生成秘钥对（公钥和私钥），把公钥上传到服务器

秘钥登陆步骤
  1、在客户机生成秘钥对（公钥和私钥）
    执行 ssh-keygen  //默认使用RSA非对称加密算法。命令会询问秘钥对存放位置，回车使用默认。passphrase也是用默认
    默认在 ~/.ssh/ 目录下生成私钥id_rsa、公钥id_rsa.pub
  2、将公钥上传到服务器
    ssh-copy-id root@192.168.3.180  这句命令等价于 ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.3.180
      需要输入192.168.3.180 root用户的密码
      ssh-copy-id命令是把客户机的公钥追加到服务器 ~/.ssh/authorized_keys 文件
  3、ssh root@192.168.3.180  客户机免密登陆

配置SSH免密登陆后任然想使用密码登陆
  ssh -o PreferredAuthentications=password -o PubKeyAuthentication=no user@host


yum install vim
运行 vimtutor 命令可以查看教程
Vim有多种模式：
  交互模式
    是Vim的默认模式，每次运行Vim就会进入这个模式
    此模式不能输入文本
    可以删除、复制、粘贴、跳转
  插入模式
    按 i 键进入插入模式，a、o键也可以
    按ESC键退出此模式
  命令模式
    按 shift + : 键进入。按ESC键也可以进入交互模式
    也称为底线命令行模式
    此模式下课运行 退出、保存 等命令
  可视模式
    不常用

:x 与 :wq 是一样的效果

交互模式下
  x 删除一个字符
  dd 剪切行
  dw 删除一个单词
  d0 删除行首到光标处的内容，不包含光标
  d$ 删除光标处到行末的内容，包含光标
  yy 复制行
  p 粘贴，只能配合前面的命令使用，Ctrl+C 复制的内容使用p无效
  r 替换，按ESC退出
  u 撤销，如果要撤销4次，按下4，再按u
  ctrl + r  重做
  :set nu  设置行号
  :set nonu  隐藏行号
  G 跳转指定行，按5加 shift+g 或者 5+gg ,跳转到第5行

  / 进入查找模式，n 下一个，N 上一个

v 进入字符可是模式，按上下键，配合d实现删除
V 进入行可视模式
Ctrl + v 块可视模式

vim的全局配置文件在 /etc/vimrc
在家目录创建一个 .vimrc 的配置文件 cp /etc/vimrc  ~/.vimrc
vim ~/.vimrc
  双引号后面的选项是被注释的

默认加入行号
自定义，显示行号，忽略大小写
set nu
set ignorecase


安装git
yum install -y git

执行 git config --global user.email chenpiqian@shenzhenpoly.com 后，git的配置文件在home目录下 .gitconfig

git config --global user.name chenpiqian


scp 网间拷贝
scp 源文件  目标文件
  源文件、目标文件格式 user@ip:file_name
  拷贝客户端文件到服务端
    scp consul.log root@192.168.3.180:/usr/local
  拷贝服务端文件到本机 /tmp/consul.log
  scp root@192.168.3.180:/usr/local/consul.log /tmp/consul.log


yum install -y bind-utils
host www.baidu.com 查看域名IP

IP地址与主机名映射 /etc/hosts
一个IP可以对应多个域名。这个相当于本地DNS列表？远程DNS宕机可用本地DNS？
::1  这是IPv6的本地地址，前面两个::表示省略了前面的0


enp0s3  en表示以太网，p0s3表示PCI接口的物理地址是（0, 3）

netstat 网络统计，net表示网络，stat是statistics的缩写，表示统计
 
netstat 
  -i  列出电脑所有的网络接口统计信息
  -uta  u显示UDP，t显示TCP，a显示所有连接状态
  -lt 列出状态是LISTEN的连接

shell种类
  sh：Bourne Shell的缩写，可以说是所有Shell的祖先
  Bash：Bourne Again Shell的缩写，是sh的进阶版，目前大多数Linux发行版默认使用Bash

.bashrc文件就是Bash的配置文件
一般以rc结尾的多为配置文件

shell脚本第一行是指定使用哪种Shell来运行它
使用sh运行
#!/bin/sh

使用bash运行
#!/bin/bash  

#!被称为Sha-bang，或者Shebang，用于指定脚本要使用的shell
如果shell脚本不写 #!shell程序路径，则会使用当前用户的shell执行脚本，这可能导致问题

可直接 ./shell文件名 执行shell，./不能少

调试模式运行  bash -x test.sh



#!/bin/bash
# bash 定义变量，等号两边不能加空格。有空格bash认为message是一个命令
message='信息'

# 使用变量要在变量前面加$
echo $message

# 单引号内全是文本，原样输出
echo '$message'

# 双引号，会解析美元符号$、反引号`、反斜杠\
echo "$message"

# 反引号，要求shell执行被反引号括起来的内容
echo `pwd`

# read：请求输入，将输入赋值给变量
#read var1
#echo "$var1"

# read一次读取多个输入，使用空格隔开
#read name1 name2
#echo "$name1  $name2"

# read -p参数，提示信息
# -n 限制输入长度
# -t 限制输入时间
# -s 隐藏内容
#read -p '请输入名字（最长5字符）：' -n 5 -t 5 yn
# echo -e 启用反斜杠转译
#echo -e "\n$yn"

# Bash中，所有变量都是字符串。但是可以使用 let 命令
# 小数运算使用 bc 命令
let "a=1"
let "b=2"
let "c=a+b"
echo $c

# 运行 env 命令查看本机环境变量，使用 $XXX 获取具体环境变量的值，例如 $PATH
# 使用 export 定义环境变量

# $# 参数个数，$0 被运行的脚本文件名，$1 第一个参数，$n 第n个参数
echo "文件名：$0"
echo "参数1：$1"

# 数组，元素之间用空格。使用${}取值
array=('v0' 'v1' 'v2')
echo "${array[2]}"

#给单个数组元素赋值
array[5]='v5'
echo "${array[5]}"

# 输出所有数组元素
echo "${array[*]}"


if条件语句基本格式，方括号与“条件”之间有一个空格
if [ 条件 ]
then
  做这个
fi
或者
if [ 条件 ]; then
  做这个
fi

在shell中等于判断是使用一个等号 = 表示，也可以使用两个等号表示

if [ 条件 ]
then
  做这个
else
  做这个  
fi

if [ 条件1 ]
then
  做这个
elif [ 条件2 ]
then
  做这个
else
  做这个  
fi


for循环，循环列出当前目录的文件
#!/bin/bash
listfile=`ls`
for file in $listfile
do
  echo "File found：$file"
done


Shell定义函数的两种方式
函数名 () {
  函数体
}

function 函数名 {

}

调用函数直接写函数名即可，不需要加括号

函数的定义
1、函数名后面的圆括号不能加任何参数
2、函数定义要放到函数调用前

shell函数传递参数，也是使用$符号。类似于函数也是shell脚本
#!/bin/bash
print_something () {
  echo "Hello $1"
}

print_something abc
print_something bcd

shell函数能返回函数执行的状态，也用return

Shell变量默认是全局变量，在shell脚本的任何地方都能使用
定义局部变量使用local关键字

################Shell 变量##################
xxx 变量名   $xxx 变量值
set 查看系统已经定义的环境变量
set -u 设定此选项，调用未声明变量时会报错
unset xxx 删除变量
env 查看系统环境变量

export 变量名=变量值
或者
变量名=变量值
export 变量名


运算
aa=11
bb=22
dd=$(expr $aa + $bb)
dd的值是aa、bb的和，注意+左右两侧必须有空格

ff=$(($aa + $bb))
a=$(( ($aa + $bb)/$aa*$bb ))


/etc/profile
/etc/profile.d/*.sh
/etc/bashrc
~/.bash_profile
~/.bashrc

etc下的环境变量，所有用户都能用
~家目录下的环境变量，只有当前用户可以使用

~/.bash_logout  退出前执行的命令
~/.bash_history   历史命令，退出登录后追加到文件中
/etc/issue  本地终端开机后的提示信息
/etc/motd  本地终端、远程终端登陆后显示信息

grep "/bin/bash" /etc/passwd

cut字符串截取
root=grep "/bin/bash" /etc/passwd | cut -f 1 -d ":"

printf '%s\t%s\t%s\t\n' 1 2 3 4 5 6

vim student.txt
ID  Name  Gender  Mark
1 furong  f 85
2 fengi f 60
3 cang  f 70

printf '%s\t%s\t%s\t%s\n' $(cat student.txt)

awk '条件1{动作1}条件2{动作2}'
awk '{printf $2 "\t" $4 "\n"}' student.txt
awk '{print $2 "\t" $4}' student.txt

cat student.txt | grep -v Name | awk '$4>=70{print $2}'


Linux特殊进程
  这些进程不与任何终端相关联，无论用户身份如何，都在后台运行，而且这些进程的父进程PID为1的进程
  PID为1的进程只在系统关闭时才会被销毁
  这些进程会在后台一直运行
  守护进程的名字通常会在最后有一个 d，表示daemon。例如：systemd、httpd、smbd
  systemd是几乎所有最新Linux发行版采用的初始化进程，systemd的PID进程号是1

05-新版本systemd与老版本system V 比较.jpg

yum install -y samba

systemctl list-units --type=service

使用journalctl命令管理日志

journalctl -u smb.service  显示smb服务的日志，u是unit的缩写

安装Apache
  yum install httpd   // 在Red Hat一族中，Apache程序的名字叫httpd。在Ubuntu系统中叫apache
  systemctl start httpd
  systemctl reload httpd  重新加载配置文件


yum install -y httpd
yum install -y mod_ssl
systemctl start httpd
httpd服务使用自签名证书
  yum install -y openssl  # openssl的配置文件/etc/pki/tls/openssl.cnf，定义了证书、私钥的默认位置
  cd /etc/httpd/
  mkdir pki
  cd pki
  1、在客户端生成秘钥
    # genrsa使用RSA加密方式，-out输出，server.key私钥，2048位
    openssl genrsa -out server.key 2048 
  2、使用客户端秘钥与客户端信息生成证书签名请求文件。客户端秘钥作用是加密客户端信息，证书签名请求文件不包含客户端秘钥
    # 生成证书签名请求，Certificate Signing Request 证书签名请求，CSR
    openssl req -new -key server.key -out server.csr
    # 填写Country Name
    CN
    # 后面是省、市、公司、部分 随便填
    # Common Name 可以你的名字或者机器的hostname，填写机器IP
    192.168.3.179
    # 填写邮箱
    xxxx
    # challenge password 和 company name 不设置，直接回车
  3、生成自签名证书，没有网可以
    # 生成证书。x509类型表示自签证书，-days 3650 有效期10年，-in表示输入，-signkey 秘钥，-out输出证书
    openssl x509 -req -days 3650 -in server.csr -signkey server.key -out server.crt
  # 拷贝私钥到 /etc/pki/tls/private
  cp server.key /etc/pki/tls/private
  # 拷贝证书到 /etc/pki/tls/certs
  cp server.crt /etc/pki/tls/certs
  # 返回上一级
  cd ../
  # 修改Apache ssl配置文件
  vim conf.d/ssl.conf
  # 证书位置
  SSLCertificateFile /etc/pki/tls/certs/server.crt
  # 私钥位置
  SSLCertificateKeyFile /etc/pki/tls/private/server.key
  # 重启httpd服务
  systemctl restart httpd
  # 浏览器使用https访问
  https://192.168.3.179/


firewall-cmd --list-ports  防火墙开放的端口


SELinux安全子系统
  SELinux是Security-Enhanced Linux的缩写，表示“安全增强型linux”

SELinux的双重保险
  域限制：对服务程序的功能进行限制
  安全上下文：对文件资源的访问限制

SELinux的三种配置模式
  enforcing    强制启用安全策略模式，将拦截服务的不合法请求
  permissive   遇到服务越权访问时，只发出警告而不强制拦截
  disabled     对于越权的行为不警告也不拦截

sestatus 查看SELinux的状态

ll -Zd /var/www/html/      显示安全策略


进入救援模式
  1、启动时按 e 键
  2、找到linux16**** root=/dev/mapper/centos-root crashkernel=auto
    改成 root=/dev/mapper/centos-root rw init=/sysroot/bin/sh crashkernel=auto
  3、按 Ctrl+x 启动，就进入了救援模式
  4、修改密码
    # 修改root
    chroot /sysroot
    # 禁用SELINUX
    vi /etc/selinux/config
      SELINUX=disabled
    passwd root
    输入新密码
    exit
    reboot

df -h     磁盘概览
free -h   内存概览
  free  未使用的内存
  buff/cache  缓冲与缓存使用的内存
  available   free加上buff/cache的内存量
uname -a  打印所有系统信息
cat /proc/cpuinfo   处理器信息
fdisk -l  磁盘分区信息


/dev device的缩写，表示“设备”，每一个/dev目录的子目录对应一个外设，比如代表光盘驱动器的文件就会出现在这个目录下面

06-dev子目录名与设备的对应关系.png

硬盘的磁盘分区命名规则，sda
  s 表示SATA或者SCSI接口的硬盘
  d 表示磁盘驱动器
  a 第三个字母使用26个字母，表示顺序
  sda表示SATA或者SCSI接口的硬盘


mount 用于挂载文件系统，挂载在Linux中的意思其实是把硬件设备与目录关联起来
  -t 指定文件系统的类型
  -a 挂载所有在/etc/fstab中定义的文件系统

vim /etc/fstab 新增挂载点
/dev/sda1 /backup xfs defults 0 0   // 把/dev/sda1硬盘挂载到/backup目录，文件类型是xfs

umount撤销挂载



VirtualBox添加一块硬盘
  设置 —> 存储 —> 控制器:SATA —> 添加虚拟硬盘 -> 创建新的虚拟盘，后面保持默认即可

ll /dev/sd*   查看新增的硬盘 /dev/sdb

fdisk 磁盘增删改查
fdisk -l

fdisk /dev/sdb  管理硬盘
  m  显示帮助信息
  p  显示分区信息
  n  创建分区
  多次回车
  结束扇区输入 +2G

  再用p命令，即可看到多了一个分区
  w 写入并退出

ll /dev/sd
  多了一个sdb1分区，sdb1是sdb硬盘的一个分区

要格式化分区，同时指定文件系统，才能使用此分区

mkfs 是make file system的缩写，表示制作文件系统，用于格式化、指定文件系统

mkfs.xfs /dev/sdb1    格式化/dev/sdb1，文件系统是xfs

格式化分区后，就可以将分区挂载到目录了
mkdir /new-fs
mount /dev/sdb1 /new-fs/  将分区挂载到目录
df -h  分区被挂载到了/net-fs目录
/dev/sdb1                2.0G   33M  2.0G    2% /new-fs  

挂载完成后，重启会失效，需要编辑vim /etc/fstab
vim /etc/fstab
/dev/sdb1 /new-fs xfs defaults 0 0
/dev/sdb2 swap   swap defaults 0 0

重启 reboot
df -h 查看分区还在

增加swap交换分区
fdisk /dev/sdb
n
回车3次
分区大小设置为 +1G
p
w
有一个警告，****reboot or after you run partprobe(8) or kpartx(8)
执行 partprobe 命令即可

mkswap /dev/sdb2
swapon /dev/sdb2
free -h  增加了swap分区容量




命令后加上 &，程序会在后台运行，但是终端关闭后，此终端下的后台进程也会比关闭
可以使用 nohup [命令] &  来运行，即关闭终端时，nohup后面的命令进程不接收SINGHUP信号



需求：查看进程中 和 vim相关的进程，并保留头部文字，以便确认哪个进程号是pid，哪个是ppid
命令：ps -ef|head -n 1;ps -ef|grep -i vim



Centos6、Centos7的变化
   系统                Centos6                     Centos7
文件系统       ext4                            xfs
修改主机名     /etc/sysconfig/network          /etc/hostname
修改时区       /etc/sysconfig/clock            timedatectl set-timezone Asia/TOkyo
查看ip信息     ifconfig                        ip
修改DNS地址    /etc/resolv.conf                -
查看端口状态   netstat                         ss   
防火墙        iptables                         firewalld
服务管理      System V init                    systemd
时间同步服务   ntp                             chrony

Centos7
  默认支持docker，内核支持OverlayFS、Repo源支持
  不再支持32位操作系统

Centos6修改时间：cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
Centos7修改时间：timedatectl set-timezone Asia/Shanghai
        主板时间修改为本地时间  timedatectl set-local-rtc 1
        主板时间还原   timedatectl set-local-rtc 0

参数补全
yum install -y bash-completion

使用ss替代了nestat
查看某种状态下的连接
ss -luntp state established

统计个数 |wc -l

通过端口查询
ss -na sport eq :22


***************************Systemd变化***************************
              Centos6         Centto7
服务管理      service         systemctl
启动项管理    chkconfig       systemctl
系统启动级别   init           systemctl
定时任务       cron           timer
日志管理       syslog         Systemd-journal

systemd通过文件类型来识别是哪种管理类型
文件拓展名      作用
.service      系统服务
.target       模拟实现运行级别
.device       定义内核识别设备
.mount        文件系统挂载点
.socket       进程间通信用的socket文件
.timer        定时器
.snapshot     文件系统快照
.swap         swap设备
.automount    自动挂载点
.path         监视文件或者目录
.scope        外部线程
.slice        分层次管理系统进程


systemctl -t help   支持的服务类型
systemctl -h 支持的参数

systemd命令

systemctl 、systemctl list-units      查看激活的单元
systemctl --failed                    运行失败的单元
systemctl list-unit-files             所有可用的单元
systemctl help <单元>                 单元的帮助手册页
systemctl daemon-reload               重新载入systemd，扫描新的或有变动的单元


systemctl start 单元   激活单元
systemctl stop 单元   停止单元
systemctl restart 单元   重启单元
systemctl reload 单元   重启单元
systemctl status 单元   重载单元
systemctl is-enabled 单元   检查单元是否配置为启动单元
systemctl enable 单元   开机激活单元
systemctl enabled --now 单元   开机启动并立即启动这个单元
systemctl disable 单元 取消开机自动激活单元
systemctl mask 单元 禁用一个单元，间接启动也不可能
systemctl unmask 单元 取消禁用的某个单元

systemctl cat 单元   查看 单元.service 文件


自定义service文件

vim imoocc_gen.sh

#!/bin/bash

# 输出当前shell的pid
echo $$ >> /var/run/imoocc_gen.pid

#循环输出
while :
do
  echo "Hi,imoocc,"$(date) >> /tmp/imoocc.res
  sleep 1
done

输出pid
cat /opt/imoocc_gen.sh 和 ps -ef|grep imoocc_gen 结果一样
拷贝一份service文件，再改改
cd /usr/lib/systemd/system
cp nginx.service imoocc_gen.service

vim imoocc_gen.service

[Unit]
Description=des
# after可以去掉
After=network.target remote-fs.target nss-lookup.target

[Service]
# 不产生子进程，使用simple（默认）
Type=simple
# 指定pid
PIDFile=/var/run/imoocc_gen.pid
# 一定要使用全路径
ExecStart=/bin/sh /opt/imoocc_gen.sh
# 停止
ExecStop=/bin/kill -s TERM $MAINPID

[Install]
WantedBy=multi-user.target

# 先reload再start
systemctl daemon-reload
systemctl start imoocc_gen.service
systemctl status imoocc_gen.service 

定时任务
/usr/lib/systemd/system/imoocc_gen.timer 
[Unit]
Description=timer unit - Print info
Documentation=http://imoocc.com

[Timer]
Unit=imoocc_gen.service
OnCalendar=2019-11-19 18:36:00

[Install]
WantedBy=multi-user.target


查看定时任务
systemctl list-timers


防火墙
/lib/firewalld/zones
启动防火墙
systemctl start firewalld.service 
查看默认区域
firewall-cmd --list-all
查看9个区域
firewall-cmd --list-all-zones
设置默认区域
firewall-cmd --set-default-zone=home
修改网卡接口区域
firewall-cmd --zone=home --change-interface=enp0s3

开放端口，阿里云还需要安全组规则
firewall-cmd --zone=home --add-port=80/tcp
开发端口后再使配置永久生效
firewall-cmd --zone=public --add-port=80/tcp --permanent
移除开放端口
firewall-cmd --zone=public --remove-port=80/tcp

重载firewall，不会关闭已经建立的连接
firewall-cmd --reload
重载firewall，会关闭所有连接
firewall-cmd --complete-reload

移除端口，使用 firewall-cmd --list-all 不会展示已经移除的端口，需要 firewall-cmd --reload  才会看到
firewall-cmd --zone=public --remove-port=80/tcp --permanent

查看firewall可用服务文件
ll /usr/lib/firewalld/services

添加服务白名单
firewall-cmd --zone=public --add-service=http

执行 firewall-cmd --list-all
services会多了http services: ssh dhcpv6-client http

# 多区域组合，默认区域是drop
firewall-cmd --set-default-zone=drop
# 192.168.3.0/24 trusted区域配置指定网段配置可以访问
firewall-cmd --zone=trusted --add-source=192.168.3.0/24 --permanent
firewall-cmd --reload


通过公网linux登陆私网linux
ssh 私网ip
yes
输入密码


GRE  深圳、杭州都创建有公网ip的ECS
两个ECS都安装GRE
查看模块是否安装  lsmod | grep ip_gre
安装模块    modprobe ip_gre

# 在 172.31.0.252 添加GRE隧道，112.74.22.58 远程公网ip，172.31.0.252是当前机器内网ip
ip tunnel add tun1 mode gre remote 112.74.22.58 local 172.31.0.252
# 激活隧道
ip link set tun1 up
# 设置互联地址
ip addr add 192.168.1.1 peer 192.168.1.2 dev tun1
# 添加路由，10.165.0.0/24 是远程公网ip ECS所在专有网络VPC的交换机
ip route add 10.165.0.0/24 dev tun1

# 在 10.165.0.8 添加GRE隧道，121.41.65.63 远程公网ip，10.165.0.8是当前机器内网ip
ip tunnel add tun1 mode gre remote 121.41.65.63 local 10.165.0.8
# 激活隧道
ip link set tun1 up
# 设置互联地址，要反过来
ip addr add 192.168.1.2 peer 192.168.1.1 dev tun1
# 添加路由，172.31.0.0/24 是远程公网ip ECS所在专有网络VPC的交换机
ip route add 172.31.0.0/24 dev tun1

公网121.41.65.63内网172.31.0.252添加安全组，信任网段10.165.0.0/24可入
公网112.74.22.58内网10.165.0.8添加安全组，信任网段172.31.0.0/24可入



#####开机启动########
脚本赋予开机启动权限
chmod +x xxx.sh

vim /etc/rc.d/rc.local
加上脚本
sh xxx.sh
可能需要执行
chmod +x /etc/rc.d/rc.local

###########后台启动jenkins应用###########
nohup $JAVA_HOME/bin/java -jar -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC  /usr/local/software/jenkins/
jenkins.war --httpPort=8888 > /tmp/jenkins.log 2>&1 &


宿主机开启ss
所有linux执行
export http_proxy=http://192.168.1.104:1080
export https_proxy=http://192.168.1.104:1080
wget https://www.google.com.hk/webhp?hl=zh-CN&sourceid=cnhp
####linux ping不通www.google.com！！但可以使用wget#####



















