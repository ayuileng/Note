* 路由：处理URL和函数之间的关系，一般采用修饰器定义。
```python
    @app.route('/user/<name>')
    def user(name):
        return '<h1>Hello, %s!</h1>' % name
```
* 被路由修饰器修饰的函数称为视图函数，视图函数的返回值称为响应，是客户端接收到的内容。视图函数返回的响应可以是HTML，也可以是复杂的表单等
* 要让视图函数访问到请求对象，这样才能处理请求，可以将请求对象传入视图函数，但是这样复杂；Flask使用上下文临时对象把某些对象当作全局对象使用，这样就不用传入视图函数了。
    1. current_app----程序上下文----当前激活程序的程序实例
    2. g--------------程序上下文----处理请求时用作临时存储的对象。每次请求都会重设这个变量
    3. request--------请求上下文----请求对象，封装了客户端发出的 HTTP 请求中的内容
    4. session--------请求上下文----用户会话，用于存储请求之间需要“记住”的值的词典
* 请求钩子：为了避免在每个视图函数中都使用重复的代码，可以注册通用函数，注册的函数可以在请求被分发到视图函数之前或者之后使用：在请求钩子函数和视图函数之间共享数据一般使用上下文全局变量g。
    1. before_first_request：注册一个函数，在处理第一个请求之前运行。
    2. before_request：注册一个函数，在每次请求之前运行。
    3. after_request：注册一个函数，如果没有未处理的异常抛出，在每次请求之后运行。
    4. teardown_request：注册一个函数，即使有未处理的异常抛出，也在每次请求之后运行。
* 如果视图函数返回的响应需要使用不同的状态吗，可以把数字码作为第二个返回值；视图函数返回的响应还可以接受第三个参数，是HTTP首部组成的词典。
* 视图函数还可以返回response对象：
```python
    @app.route('/')
    def index():
        response = make_response('<h1>This document carries a cookie!</h1>')
        response.set_cookie('answer', '42')
        return response
```
* 有一种名为重定向的特殊响应类型。 这种响应没有页面文档，只告诉浏览器一个新地址用以加载新页面。一般在表单提交的时候使用，防止重复提交(redirect after post)。
* 重定向通常使用状态吗302表示，指向的地址由HTTP首部Location中提供；也可以使用Flask中的redirect()函数：
```python
    @app.route('/')
    def index():
        return redirect('http://www.example.com')
```
* Flask使用Jinja2的模版渲染引擎；默认情况下会在template文件夹下寻找模版；使用render_template()函数进行渲染，render_template 函数的第一个参数是模板的文件名。 随后的参数都是键值对，表示模板中变量对应的真实值。
```python
    @app.route('/user/<name>')
    def user(name):
        return render_template('user.html', userName=name)
```
* 使用过滤器修改变量，过滤器名添加在变量名之后，中间使用竖线分隔:`Hello, {{ name|capitalize }}`
* Jinja2还支持宏，类似于函数：
```html
    {% macro render_comment(comment) %}
        <li>{{ comment }}</li>
    {% endmacro %}

    <ul>
        {% for comment in comments %}
            {{ render_comment(comment) }}
        {% endfor %}
    </ul>

    {% import 'macros.html' as macros %}
    <ul>
        {% for comment in comments %}
            {{ macros.render_comment(comment) }}
        {% endfor %}
    </ul>
```
* 模板继承：
```html
    <html>
        <head>
            {% block head %}
                <title>{% block title %}{% endblock %} - My Application</title>
            {% endblock %}
        </head>
        <body>
            {% block body %}
            {% endblock %}
        </body>
    </html>

    {% extends "base.html" %}
    {% block title %}Index{% endblock %}
    {% block head %}
        {{ super() }}
    {% endblock %}
    {% block body %}
        <h1>Hello, World!</h1>
    {% endblock %}
```
* 可以用模板继承机制少写很多重复的代码，比如自定义404和500页面时，可以继承base页面等。
* url_for()辅助函数：以视图函数名作为参数,第二个参数`_external=True`表示绝对地址。生成动态地址时可以将动态部分作为关键字参数传入:`url_for('user', name='john', _external=True) `。
* static路由：对静态文件的引用被当做静态路由，即`/static/<lename>`，默认情况下会在static文件夹下查找静态文件：`url_for('static', filename='css/styles.css', _external=True)`。
* 使用Flask-WTF来处理web表单：每个web表单都是继承于Form类，这个类定义了表单中的各种字段，每个字段后面可以加上验证函数，字段对象的构造函数的第一个参数是渲染成HTML时标号：
```python
    class NameForm(Form):
        name = StringField('What is your name?', validators=[Required()])
        submit = SubmitField('Submit')
```
* 表单在模板中调用后就会渲染成相应的HTML，

