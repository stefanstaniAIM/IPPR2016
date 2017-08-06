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
            data: {
              menu: {
                title: 'Prozessmodelle'
              }
            },
            children: [
              {
                path: 'models',
                data: {
                  menu: {
                    title: 'Anzeigen'
                  }
                }
              },
              {
                path: 'import',
                data: {
                  menu: {
                    title: 'Importieren'
                  }
                }
              }
            ]
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
          },
          {
            path: 'eventLogger',
            data: {
              menu: {
                title: 'Event-Logger'
              }
            }
          },
          {
            path: 'manipulatePNML',
            data: {
              menu: {
                title: 'PNML manipulieren'
              }
            }
          },
          {
            path: 'generateOWL',
            data: {
              menu: {
                title: 'OWL generieren'
              }
            }
          }
        ]
      }
    ]
  }
];
