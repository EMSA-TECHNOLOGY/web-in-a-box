import Ember from 'ember';
import DS from 'ember-data';

export default DS.Transform.extend({
  deserialize: function (serialized) {
    var date = serialized["$date"];
    var type = typeof date;

    if (type === 'string') {
      return new Date(Ember.Date.parse(date));
    } else if (type === 'number') {
      return new Date(date);
    } else if (date === null || date === undefined) {
      // if the value is null return null
      // if the value is not present in the data return undefined
      return date;
    } else {
      return null;
    }
  },

  serialize: function(deserialized) {
    return deserialized;
  }
});
