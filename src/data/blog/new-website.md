---
title: "New website"
description: "Maybe now I'll actually blog."
pubDatetime: 2026-04-05
tags: ["website", "blog"]
---

I rebuilt my website. And by "I" and "rebuilt", I mean I asked Claude Code to basically run Astro's project generator.

I built [the previous iteration of my website][1] while in Covid lockdown in 2020. I was working on a few fun side projects while stuck at home, and I figured it'd be fun to blog about them. I set up a GitHub Pages site using Jekyll, wrote [a couple meta blog posts][2] about the development process, and then of course never added anything else.

In contrast, the site's infrastructure changed _substantially_ over time. I moved hosting to Netlify [in 2021][3] (apparently it was "simpler to manage" than GitHub Pages, because this tiny website was too much for me to handle) and replaced Jekyll with a custom static site generator written in Clojure [in 2023][4].

That hand-rolled SSG was fun to build at the time, but ultimately got in the way of me actually writing. I never needed anything too sophisticated, but I became annoyed with how my site looked and acted and felt stuck with it.

With the advent of coding agents, I've been thinking that maybe I'd put one to work enhancing my custom generator to build the site I wanted.

Then I decided it'd be a lot easier to just use a real SSG with a nice theme.

I'm sure I'll customize things more in the future. But for now, this is pretty off-the-shelf, and I've never been happier with my website.


[1]: https://web.archive.org/web/20240424105921/https://tylerkindy.com/
[2]: https://web.archive.org/web/20250824153954/https://tylerkindy.com/blog/
[3]: https://github.com/tkindy/website/commit/6a05305dc77d4b20c174a69baeae87b07bf3adc6
[4]: https://github.com/tkindy/website/pull/12