INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES
    (1, '1984', 'G. Orwell', '9781234567897', 5.95, false),
    (2, 'Tarzan', 'Tarzan', '9789876637889', 7.95, false),
    (3, 'Lisova Mavka', 'L. Ukrainka', '9781122334111', 12.95, false);

INSERT INTO categories(id, name)
VALUES
    (1, 'Horror'),
    (2, 'Adventures');

INSERT INTO books_categories(books_id, categories_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 1);
