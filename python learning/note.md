* `/`是普通除法，如果是整数相除会向下取整，如果想要精确的话应该改用浮点数
* `//`是整除，不管是什么类型都会向下取整
* `import`模块之后可以用模块.函数的格式来调用；如果是`from 模块 import 函数`，则可以直接调用函数
* 值被转换为字符串的两种机制：`str`函数把值转换为合理的字符串；`repr`函数会创建一个字符串，以合法的Python表达式的形式来表示值：
```python
  print repr('hello world') #'hello world'
  print repr(1000L)         #1000L
```
* `input()`和`raw_input()`的区别：input不会自动把接受的数据转为字符串
* r''表示不转义
* 列表和元组的区别：元组是不能修改的列表
* 分片：第一个索引是要提取的第一个元素的下标，第二个索引是分片后剩余部分的第一个元素的下标,如果分片要分到最后一个元素，则第二个索引为空就行,同样适用于开始：
```python
  nums = [1,2,3,4,5,6,7,8,9,10]
  nums[3:6]     #[4,5,6]
  nums[0:1]     #[1]
  nums[-3:]     #[8,9,10]
```
* 分片还可以指定步长,如果步长是负数则表示从右往左进行：
```python
  nums[0:10:2]      #[1,3,5,7,9]
  nums[0:10:-2]     #[10,8,6,4,2]
```
* 序列相加表示拼接，序列相乘表示重复
* `list()`函数：将字符串转换为列表；不能给列表不存在的索引处赋值！！(与ruby的区别)；del 语句或函数：删除列表项；分片赋值：灵活
* 列表方法：
```python
    1. append()     #在末尾追加
    2. count()      #统计某元素出现次数
    3. extend()     #在末尾追加一个序列
    4. index()      #返回元素首次出现的下标
    5. insert()     #在某位置前插入元素
    6. pop()        #删除并返回最后一个元素
    7. remove()     #删除列表中某个值的首个匹配项
    8. reverse()    #倒置
    9. sort()       #排序
```
* 元组：不可变的列表；创建元组：用逗号分隔或者圆括号括起来；一个值的元组(1,)
* `tuple()`函数：将序列转为元组
* 字符串格式化：
```python
  format = "hello, %s. %s enough for ya?"
  values = ('world', 'Hot')
  print format % values     #hello, world. Hot enough for ya ?
```
* 字符串方法：
```python
    1. find()         #查找字串的最左端索引，若无则为-1，另外两个参数为起点终点
    2. join()         #链接序列中的元素    
    3. lower()        #返回小写版
    4. replace()      #将所有匹配项替换
    5. split()        #分割
    6. strip()        #去除两边的空白
    7. translate()    #很少用
```
* 用`dict()`函数将其他映射或者键值对序列建立成字典：
```python
  items = [('name','yajima'),('age','42')]
  dict(items)     #{'name': 'yajima','age': '42' }
  dict(name='yajima', age=42)
```
* 字典的格式化字符串：
```python
  phonebook = ['aaa': '9999', 'bbb': '8888']
  "bbb's phonenum is %(bbb)s" % phonebook
```
* 字典方法：
```
  1. clear()        #清除所有项
  2. copy()         #浅复制，当在副本中替换了值的时候原字典不受影响，如果是修改了值的话，原始字典也会变化
  3. fromkeys()     #用给定的键建立新字典，默认的值是None
  4. get()          #防止key不存在时出错
  5. has_key()      #检查是否有键，3.0中已经不存在了
  6. items()        #将所有项以列表的形式返回，每一项都是键值对
  7. iteritems()    #同上，但是是返回迭代器对象
  8. keys()         #将字典中的键以列表形式返回
  9. iterkeys()     #同上，但是是返回迭代器对象
  10. pop()         #获得给定键的值，之后删除这个键值对
  11. popitem()     #弹出随机的一个键值对(与列表的pop的区别在于没有顺序)
  12. setdefault()  #当键不存在时则返回默认值，如果存在就返回值
  13. update()      #用一个键值对来更新字典，会覆盖原有的
  14. values()      #返回所有值的列表
  15. itervalues()  #同上，但是是返回迭代器对象
```
```python
  x={'a': 'A', 'b': ['B', 'BB']}
  y = copy(x)
  y['a'] = 'AA'
  y['b'].remove('BB')
  y      #{'a': 'AA', 'b': ['B']}
  x      #{'a': 'A', 'b': ['B']}

  {}.fromkeys(['name', 'age'])
  {'age': None, 'name': None}
```
* 序列解包：将多个值的序列解开，然后放到变量的序列中(个数必须一一对应，与ruby的区别所在)。
```python
  values = 1, 2, 3
  print values      #(1, 2, 3)
  x, y, z = values
```
* 链式赋值：从右往左依次进行
* python中None，0，""，‘’，{}，[]等都是false。(与ruby不同)
* `is`运算符： `x is y`表示x和y是否是同一个对象
* 并行迭代：`zip()`函数可以应付不等长的序列，只要短的序列结束便会自动结束
```python
  names = ['a','b','c']
  ages = [12,34,56]
  for name, age in zip(names, ages):
    print name, age
```
* 编号迭代：使用`enumerate()`函数：
```python
  for index, string in enumerate(strings):
    strings[index] = string + "hehe"
```
* 翻转和排序迭代：`reversed()`和`sorted()`函数可以作用在任何序列或者可迭代对象上，返回翻转或者排序后的版本，而不是在原对象上面修改
* for循环中的`else子句`：当for循环体中没有调用break时会执行
* 列表推导式：`[x*x for x in range(10) if x%3 == 0]`
* 位置参数和关键字参数：位置参数就是原本的形式，关键字参数类似于有默认值的形参定义；位置参数必须放在关键字参数之前
* 可变参数：func(*params)，params会解释为元组；func(**params)，params会被解释为字典
```python
  def story(**kwds):
      return 'Once upon a time,there was a %(job)s called %(name)s.' % kwds

  def poewr(x, y, *others):
      if others:
          print 'Received redundant parameters:', others
      return pow(x, y)

  def interval(start, stop=None, step=1):
      'Imitates range() for step > 0'
      if stop is None:
          start, stop = 0, start
      result = []
      i = start
      while i < stop:
          result.append(i)
          i += step
      return result
```
* 作用域：在python中变量和值对应存在一个键值对关系，用vars()函数能返回。这个字典叫做作用域或者命名空间,除了全局作用域外，每次函数调用都会创建一个新的作用域。函数内的变量处于局部作用域，并不能影响全局作用域(在局部作用域中也能访问全局变量，但是这样容易出现问题)：
```python
  x = 1
  scope = vars()
  print scope['x']   #1

  def combine(parameter):
    print parameter+globals()['parameter']

  parameter = 'hehe'
  combine('sb')   #sbhehe

  x = 1
  def change_global():
      global x
      x += 1

  change_global()
  print x      #2

```
* 递归。例：
```python
  def search(sequence, number, lower, upper):
      if lower == upper:
          assert number == sequence[upper]
          return upper
      else:
          middle = (lower + upper) / 2
          if number > sequence[middle]:
              return search(sequence, number, middle + 1, upper)
          else:
              return search(sequence, number, lower, middle)
```

