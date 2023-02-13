{:title "Testing a Jekyll site on mobile during development"}

In my last post, I talked about a problem I'd had with flexbox on iOS. I'd only found the problem after I'd published my changes, and even though I'm sure no one's visiting my website, I didn't like the idea of breaking the public version during testing.

When I first found the problem, I tried recreating it on my laptop using mobile emulation in Firefox. However, the problem wasn't manifesting there. I wanted to test out my site locally but on my phone.

Since I host my site on [GitHub Pages][1], I use [Jekyll][2], a static site generator, to... well, statically generate my site. During development, Jekyll will run a local webserver you can connect to to test out your local changes. Changing any file causes Jekyll to regenerate your site automatically.

It's easy to connect from my laptop using `127.0.0.1` or `localhost`. But I wasn't sure if it was even possible to connect from my phone to the webserver running on my laptop given how much more locked down networking is when using Xfinity's router and service (definitely a blog post for another day).

First, I found my laptop's IP address

```
> ip address
```

This returned several addresses; the loopback address (`localhost`, `127.0.0.1`), several for my WiFi interface, and a couple for an interface named `docker0` which I'm assuming is somehow involved in Docker networking. As for the several for my WiFi interface, it was assigned one IPv4 address, and several IPv6 addresses with odd keywords after them that I don't really understand.

Anyways, I took the WiFi IPv4 address while the Jekyll server was running and tried connecting to that from my phone.

{% include image.html file="jekyll-connection-refused.png" alt="connection refused" caption="Connection refused" class="tall" %}

The connection was refused. I was thinking that maybe I needed to open the port on my laptop's firewall. I run Ubuntu, and after some searching, I found the command to do so:

```
> sudo ufw allow 4000
```

where 4000 is the default Jekyll port. I tried connecting again, but still got a "Connection refused" message.

I don't know much about networking (I really enjoyed the class on the subject in college, but a lot of it's slipped my mind), so I wasn't really sure what was going on. I did notice that even when I used my laptop's IP from my laptop, I still couldn't connect to the site, so it seemed like something was wrong with the Jekyll server.

I checked the options and found an option that sounded promising.

```
> bundle exec jekyll help serve
jekyll serve -- Serve your site locally

Usage:

  jekyll serve [options]

Options:
...
-H, --host [HOST]  Host to bind to
```

I searched online and found [this Stack Overflow answer][3] which said to use `0.0.0.0` for the host parameter to let Jekyll bind to all IP addresses, not just `localhost`. I added that option, tried again from my phone, and...

{% include image.html file="jekyll-connection-success.jpg" alt="success!" caption="Success!" class="tall" %}

... it worked! Now, I could try out different solutions or changes and see both how they look on desktop and mobile without having to screw with my public site. This was super useful in testing out fixes for my mobile flexbox issue.

[1]: https://pages.github.com/
[2]: https://jekyllrb.com/
[3]: https://stackoverflow.com/a/16608698
