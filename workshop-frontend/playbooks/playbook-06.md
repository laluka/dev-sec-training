# playbook-06.md - Prototype Pollution

Welcome to this sixth playbook, here you'll learn about Prototype Pollution.

## Outdated Components

### Goal

Fix the issue to forbid a malicious actor to arbitrary modify any object of the DOM using Prototype Pollution

### Test

```bash
# Test feature
http://localhost:3000/pollution?obj={%22name%22:%22NotRick%22,%20%22favorite_sport%22:%22Singing%22}

# Test Exploit
http://localhost:3000/pollution?obj={%22__proto__%22:{%22admin%22:true}}
```

### Understand the exploit

Prototype pollution is a security vulnerability that can occur when the properties of an object are modified in such a way that it affects other objects that are based on that prototype. This can happen in both front-end and back-end code, and it can have a number of different impacts depending on the nature of the code. In front-end code, it can result in the loss of data or the execution of unintended code. In back-end code, it can cause data corruption or allow an attacker to gain access to sensitive data. Prototype pollution is a relatively new issue, and it is not well understood. As such, it is difficult to protect against. The best defense is to avoid using code that is susceptible to this type of attack. 

One way that prototype pollution can occur is when code modifies the prototype of an object in a way that affects other objects that are based on that prototype. For example, consider the following code:

```js
var obj1 = {}; var obj2 = Object.create(obj1); obj1.foo = "bar"; console.log(obj2.foo); // "bar"
```
In this code, the prototype of obj2 is set to obj1 . When the property foo is added to obj1 , it is also added to obj2 . This can lead to unexpected results, as shown in the example above.

Another way that prototype pollution can occur is when code modifies the prototype of an object in a way that allows unintended code to execute. For example, consider the following code:
```js
var obj1 = {}; var obj2 = Object.create(obj1); obj1.foo = function() { console.log("bar"); } obj2.foo(); // "bar"
```
In this code, the prototype of obj2 is set to obj1 . When the property foo is added to obj1 , it is also added to obj2 . When foo is invoked on obj2 , the function defined in obj1 is executed. This can lead to unintended code execution, as shown in the example above.

Moreover, prototype pollution could be also be a problem when an attacker can modify the  `__proto__` property of an object. This can lead to unexpected behavior and security vulnerabilities.

For instance, consider the following code snippet:

```js
var obj = {}; obj.__proto__.foo = "bar"; console.log(obj.foo); // "bar"
```
In this code, we create an object and then overwrite its `__proto__` property with a new value. As a result, when we try to access the foo property of our object, we get the value "bar" instead of undefined .

This behavior can be exploited by attackers to bypass security checks or cause other unexpected behavior. For example, an attacker could overwrite the `__proto__` property of the Object constructor itself, causing all objects created after that point to inherit the attacker-defined properties and methods:

```js
Object.prototype.__proto__ = {}; var obj = {}; console.log(obj.hasOwnProperty); // undefined
```

As you can see, this code snippet causes the hasOwnProperty method to be undefined for all objects. This could lead to security vulnerabilities if an attacker is able to exploit this behavior in a specific application.

To avoid prototype pollution, it is important to only set the `__proto__` property of objects to values that are trusted and will not be overwritten by untrusted input.