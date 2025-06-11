import React from 'react';
import { Link } from 'react-router-dom';
import './list_item.css';

const ListItem = ({ link, title, columns, templateColumns }) => {
  return (
    <Link to={link} className="list-item-link">
      <div
        className="list-item"
        style={{ gridTemplateColumns: templateColumns }}
      >
        <span className="list-item-title">{title}</span>
        {columns.map((column, index) => (
          <span key={index} className={column.className}>
            {column.content}
          </span>
        ))}
      </div>
    </Link>
  );
};

export default ListItem;
