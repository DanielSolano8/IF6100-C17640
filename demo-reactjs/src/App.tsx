import { useState } from 'react';
import './App.css';

function App() {
	const [name, setName] = useState('');
  const [names, setNames] = useState<string[]>([]);

  const addName = () => {

    console.log(names); 
    setNames([...names, name]); 
  };


	return (
    <>
  <div>{name}</div>
  <div>
    
    <input
      type="text"
      value={name} onChange={(e) => setName(e.target.value)}/>
      <button onClick={addName}>Agregar</button>

    <ul>

      {names.map((name, index) => (<li key={index}>{name}</li>))}


    </ul>



     </div>

   </>
  );
}

export default App;
