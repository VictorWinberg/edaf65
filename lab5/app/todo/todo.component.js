(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: 'todo',
      templateUrl: 'app/todo/todo.component.html',
    })
  ];

  function TodoComponent() {
    this.todo = {};
  }
})(window.app = window.app || {});
