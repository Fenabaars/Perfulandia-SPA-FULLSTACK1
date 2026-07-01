import os
import glob

def prop_to_dict(lines):
    result = {}
    for line in lines:
        line = line.strip()
        if not line or line.startswith('#'):
            continue
        if '=' not in line:
            continue
        key, value = line.split('=', 1)
        key = key.strip()
        value = value.strip()
        
        parts = key.split('.')
        current = result
        for i, part in enumerate(parts):
            if i == len(parts) - 1:
                current[part] = value
            else:
                if part not in current:
                    current[part] = {}
                current = current[part]
    return result

def dict_to_yaml(d, indent=0):
    yaml_str = ""
    for k, v in d.items():
        yaml_str += "  " * indent + f"{k}:"
        if isinstance(v, dict):
            yaml_str += "\n" + dict_to_yaml(v, indent + 1)
        else:
            yaml_str += f" {v}\n"
    return yaml_str

def main():
    search_path = "c:\\Users\\fena\\Desktop\\perfulandia\\Perfulandia-SPA-FULLSTACK1\\**\\src\\main\\resources\\application.properties"
    files = glob.glob(search_path, recursive=True)
    
    for f in files:
        with open(f, 'r', encoding='utf-8') as file:
            lines = file.readlines()
        
        d = prop_to_dict(lines)
        yaml_content = dict_to_yaml(d)
        
        # Agregamos perfiles por defecto según la rúbrica (dev/prod)
        yaml_content += "\n---\nspring:\n  config:\n    activate:\n      on-profile: dev\n"
        
        yml_path = f.replace('.properties', '.yml')
        with open(yml_path, 'w', encoding='utf-8') as file:
            file.write(yaml_content)
            
        os.remove(f)
        print(f"Converted {f} to {yml_path}")

if __name__ == "__main__":
    main()
