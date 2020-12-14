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
#   sort -n << END   按END退出

# wc -m << END
# > How many characters are there in this sentence ?
# > END

| 管道符号


系统监控
  w命令、uptime命令、tload命令
   

ps

