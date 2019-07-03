安装ruby
wget https://cache.ruby-lang.org/pub/ruby/2.6/ruby-2.6.3.tar.gz
tar -zxvf ruby-2.6.3.tar.gz
cd ruby-2.6.3/ 
./configure -prefix=/usr/local/ruby
make
make install
cd /usr/local/ruby/
cp bin/ruby /usr/local/bin/
cp bin/gem /usr/local/bin/

ruby --version














