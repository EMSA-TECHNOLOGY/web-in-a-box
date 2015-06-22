import { moduleForModel, test } from 'ember-qunit';

moduleForModel('admin/log', 'Unit | Model | admin/log', {
  // Specify the other units that are required for this test.
  needs: []
});

test('it exists', function(assert) {
  var model = this.subject();
  // var store = this.store();
  assert.ok(!!model);
});
