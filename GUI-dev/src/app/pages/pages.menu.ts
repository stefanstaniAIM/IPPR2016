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
        path: 'myprocesses',
        data: {
          menu: {
            title: 'Prozesse',
            icon: 'ion-clipboard',
            selected: false,
            expanded: false,
            order: 2,
          }
        },
        children: [
         {
            path: 'startable',
            data: {
              menu: {
                title: 'Prozess starten'
              }
            }
          },
          {
            path: 'active',
            data: {
              menu: {
                title: 'Aktive Prozesse'
              }
            }
          },
          {
            path: 'terminated',
            data: {
              menu: {
                title: 'Beendete Prozesse'
              }
            }
          }
        ]
      },
      {
        path: 'admin',
        data: {
          menu: {
            title: 'Admin',
            icon: 'ion-android-settings',
            selected: false,
            expanded: false,
            order: 3,
            isAdmin: true
          }
        },
        children: [
         {
            path: 'models',
            data: {
              menu: {
                title: 'Prozessmodelle'
              }
            }
          },
          {
            path: 'active',
            data: {
              menu: {
                title: 'Aktive Prozesse'
              }
            }
          },
          {
            path: 'terminated',
            data: {
              menu: {
                title: 'Beendete Prozesse'
              }
            }
          }
        ]
      }
    ]
  }
];
