DROP TABLE IF EXISTS si_base_unit;
DROP TABLE IF EXISTS si_derived_unit;
DROP TABLE IF EXISTS component;
DROP TABLE IF EXISTS base_unit;

CREATE TABLE si_base_unit (
  base_symbol TEXT
);

CREATE TABLE base_unit (
  si     TEXT,
  symbol TEXT,
  scalar REAL
);

CREATE TABLE si_derived_unit (
  derived_symbol TEXT
);

CREATE TABLE component (
  derived_symbol TEXT,
  base_symbol    TEXT,
  numerator      INTEGER,
  denominator    INTEGER
);

INSERT INTO si_base_unit VALUES ('kg');
INSERT INTO si_base_unit VALUES ('m');
INSERT INTO si_base_unit VALUES ('s');
INSERT INTO si_derived_unit VALUES ('N');
INSERT INTO component VALUES ('N', 'kg', 1, 1);
INSERT INTO component VALUES ('N', 'm', 1, 1);
INSERT INTO component VALUES ('N', 's', -2, 1);
INSERT INTO base_unit VALUES ('kg', 'g', 1 / 1000.);
