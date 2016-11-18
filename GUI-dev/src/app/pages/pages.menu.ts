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
        path: 'processModels',
        data: {
          menu: {
            title: 'Prozessmodelle',
            icon: 'ion-document',
            selected: false,
            expanded: false,
            order: 1,
          }
        }
      },
      {
        path: 'processes',
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
            title: 'admin',
            icon: 'ion-android-login',
            selected: false,
            expanded: false,
            order: 3,
            isAdmin: true
          }
        }
      }
    ]
  }
];
