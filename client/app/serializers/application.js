import DS from 'ember-data';

export default DS.RESTSerializer.extend({
  normalizeId: function(hash) {
    hash.id = hash["_id"]["$oid"];
    delete hash["_id"];
  }
});
