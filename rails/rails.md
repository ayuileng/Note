### rails数据验证
* 简单实例
```ruby
class Person < ActiveRecord::Base
  validates :name, presence: true
end
 
Person.create(name: "John Doe").valid? # => true
Person.create(name: nil).valid? # => false
```
* 在view中进行数据验证是不靠谱的，应该尽量在model中完成，通过验证的数据才能存入database。如果在view中用js验证的话很不安全，容易被禁用或者注入；在控制器中验证的话不易于扩展，而且增加控制器的逻辑复杂性。
* 新建并保存记录会在数据库中执行 SQL INSERT 操作。更新现有的记录会在数据库上执行 SQL UPDATE 操作。一般情况下，数据验证发生在这些 SQL 操作执行之前。如果验证失败，对象会被标记为不合法，Active Record 不会向数据库发送 INSERT 或 UPDATE 指令。这样就可以避免把不合法的数据存入数据库。**注意，使用 new 方法初始化对象时，即使不合法也不会报错，因为这时还没做数据验证。**
```ruby
>> p = Person.new
# => #<Person id: nil, name: nil>
>> p.errors.messages
# => {}
 
>> p.valid?
# => false
>> p.errors.messages
# => {name:["can't be blank"]}
 
>> p = Person.create
# => #<Person id: nil, name: nil>
>> p.errors.messages
# => {name:["can't be blank"]}
 
>> p.save
# => false
```
* Rails 使用 valid? 方法检查对象是否合法。valid? 方法会触发数据验证，如果对象上没有错误，就返回 true，否则返回 false；Active Record 验证结束后，所有发现的错误都可以通过实例方法 errors.messages 获取，该方法返回一个错误集合。
* rails内置的数据验证helper方法(常用的)：
  1. validates_associated：如果模型和其他模型有关联，也要验证关联的模型对象，可以使用这个方法。保存对象时，会在相关联的每个对象上调用 valid? 方法。**不要在关联的两端都使用 validates_associated，这样会生成一个循环。**
  2. confirmation：如果要检查两个文本字段的值是否完全相同，可以使用这个帮助方法。例如，确认 Email 地址或密码。这个帮助方法会创建一个虚拟属性，其名字为要验证的属性名后加 _confirmation。
  ```ruby
  #在模型中
  class Person < ActiveRecord::Base
    validates :email, confirmation: true
    validates :email_confirmation, presence: true
  end
  #在视图中
  <%= text_field :person, :email %>
  <%= text_field :person, :email_confirmation %>
  ```
  3. exclusion：检查属性的值是否不在指定的集合中。集合可以是任何一种可枚举的对象。
  ```ruby
  class Account < ActiveRecord::Base
    validates :subdomain, exclusion: { in: %w(www us ca jp),
      message: "%{value} is reserved." }
  end
  ```
  4. format:这个帮助方法检查属性的值是否匹配 :with 选项指定的正则表达式。
  ```ruby
  class Account < ActiveRecord::Base
    validates :subdomain, validates :legacy_code, format: { with: /\A[a-zA-Z]+\z/,message: "only allows letters" }
  end
  ```
  5. length:默认的错误消息根据长度验证类型而有所不同，还是可以 :message 定制。定制消息时，可以使用 :wrong_length、:too_long 和 :too_short 选项，%{count} 表示长度限制的值。
  ```ruby
    class Person < ActiveRecord::Base
    validates :name, length: { minimum: 2 }
    validates :bio, length: { maximum: 500 }
    validates :password, length: { in: 6..20 }
    validates :registration_number, length: { is: 6 }

    validates :bio, length: { maximum: 1000,
      too_long: "%{countcharacters is the maximum allowed" }
  end
  ```
  6. numericality:这个帮助方法检查属性的值是否值包含数字。默认情况下，匹配的值是可选的正负符号后加整数或浮点数。如果只接受整数，可以把 :only_integer 选项设为 true。`:greater_than、:greater_than_or_equal_to、:equal_to、:less_than、:less_than_or_equal_to、:odd、:even`。
  7. presence:这个帮助方法检查指定的属性是否为非空值，调用 blank? 方法检查值是否为 nil 或空字符串，即空字符串或只包含空白的字符串。
  8. uniqueness:这个帮助方法会在保存对象之前验证属性值是否是唯一的。**该方法不会在数据库中创建唯一性约束，所以有可能两个数据库连接创建的记录字段的值是相同的。为了避免出现这种问题，要在数据库的字段上建立唯一性索引**。
* 条件验证：条件可通过 :if 和 :unless 选项指定，这两个选项的值可以是 Symbol、字符串、Proc 或数组。
  1. :if 和 :unless 选项的值为 Symbol 时，表示要在验证之前执行对应的方法;当多个条件规定验证是否应该执行时，可以使用数组。
  ```ruby
  class Order < ActiveRecord::Base
    validates :card_number, presence: true, if: :paid_with_card?
  
    def paid_with_card?
      payment_type == "card"
    end
  end
  ```
  2. 有时同一个条件会用在多个验证上，这时可以使用 with_options 方法：
  ```ruby
  class User < ActiveRecord::Base
    with_options if: :is_admin? do |admin|
      admin.validates :password, length: { minimum: 10 }
      admin.validates :email, presence: true
    end
  end
  ```
* 自定义验证方法：
  1. 自定义验证类：自定义的验证类继承自 ActiveModel::Validator，必须实现 validate 方法，传入的参数是要验证的记录，然后验证这个记录是否合法。自定义的验证类通过 validates_with 方法调用。
  ```ruby
  class MyValidator < ActiveModel::Validator
    def validate(record)
      unless record.name.starts_with? 'X'
        record.errors[:name] << 'Need a name starting with X please!'
      end
    end
  end
  
  class Person
    include ActiveModel::Validations
    validates_with MyValidator
  end
  ```
  2. 自定义状态和symbol：使用 validate 方法注册自定义验证方法时可以设置 :on 选项，执行什么时候运行。:on 的可选值为 :create 和 :update。
  ```ruby
  class Invoice < ActiveRecord::Base
    validate :expiration_date_cannot_be_in_the_past,on: :create,
      :discount_cannot_be_greater_than_total_value
  
    def expiration_date_cannot_be_in_the_past
      if expiration_date.present? && expiration_date < Date.today
        errors.add(:expiration_date, "can't be in the past")
      end
    end
  
    def discount_cannot_be_greater_than_total_value
      if discount > total_value
        errors.add(:discount, "can't be greater than total value")
      end
    end
  end
  ```
* 在视图中显示验证错误:
  ```erb
  <% if @post.errors.any? %>
    <div id="error_explanation">
      <h2><%= pluralize(@post.errors.count, "error") %> prohibited this post from being saved:</h2>
  
      <ul>
      <% @post.errors.full_messages.each do |msg| %>
        <li><%= msg %></li>
      <% end %>
      </ul>
    </div>
  <% end %>
  ```

### ruby中hash的symbol写法
```ruby
inst_section = {
  :a => 'A',
  :b => 'B',
  :c => 'C',
}
#等同于
inst_section = {
  a: 'A',
  b: 'B',
  c: 'C',
}

redirect_to action: 'show', id: product.id
#等同于
redirect_to({:action => 'show', :id => product.id})
```
