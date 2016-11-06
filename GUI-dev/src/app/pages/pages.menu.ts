export const PAGES_MENU = [
  {
    path: 'pages',
    children: [
      {
        path: 'dashboard',
        data: {
          menu: {
            title: 'Dashboard',
            icon: 'ion-android-home',
            selected: false,
            expanded: false,
            order: 0
          }
        }
      },
      {
        path: 'admin',
        data: {
          menu: {
            title: 'admin',
            icon: 'ion-android-login',
            selected: false,
            expanded: false,
            order: 1
          }
        }
      }
    ]
  }
];
