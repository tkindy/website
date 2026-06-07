---
title: "Trouble with flexbox on iOS"
description: "Debugging a flexbox layout issue on iOS Safari where images exceeded their flex-basis constraint."
tags: ["css", "flexbox", "ios"]
---

In preparation for starting this blog, I decided to clean up my website a bit. The biggest housekeeping task was throwing out Bootstrap since I was really only using it for the grid layout. In its place, I decided to try out flexbox.

If you're unfamiliar with flexbox, MDN has [a great tutorial series on it][1]. At its core, it's a CSS mechanism for flexibly laying out elements in one dimension (either horizontally or vertically). [My site's homepage][2] now uses flexbox to display my picture and the accompanying text side-by-side in a responsive way.

![Homepage built with flexbox](../../assets/images/flexbox-homepage.png)

This worked great when I viewed it on my laptop. But after I'd published my changes, I checked it out on my phone, and the image was huge.

![Supersized me](../../assets/images/flexbox-large-image.jpg)

For whatever reason, this behavior didn't show up in Firefox's mobile emulator. I was confused because I had the following CSS rule:

```css
.me-img {
  flex: 0 1 300px;
}
```

This should mean that the image starts at 300px by default and can shrink if there isn't enough space, but it can't grow. However, as you can see, the image appears to be larger than that `flex-basis`.

Unfortunately, I still haven't been able to figure out what's happening here. As a workaround, I included a `max-width` constraint on the image of the same size as the `flex-basis`. That seems to have properly clamped the size on mobile.

![Regular-sized me](../../assets/images/flexbox-normal-image.jpg)

[1]: https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/flexbox
[2]: https://tylerkindy.com
