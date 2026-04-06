export const SITE = {
  website: "https://tylerkindy.com/",
  author: "Tyler Kindy",
  profile: "https://tylerkindy.com/",
  desc: "Tyler Kindy's personal website and blog.",
  title: "Tyler Kindy",
  lightAndDarkMode: true,
  postPerIndex: 4,
  postPerPage: 4,
  scheduledPostMargin: 15 * 60 * 1000, // 15 minutes
  showArchives: false,
  showBackButton: true,
  editPost: {
    enabled: false,
    text: "",
    url: "",
  },
  dynamicOgImage: true,
  dir: "ltr",
  lang: "en",
  timezone: "America/New_York",
} as const;
