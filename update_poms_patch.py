import os

def update_pom(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    new_content = content.replace('<version>3.4.4</version>', '<version>3.4.13</version>')

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

# Find all pom.xml files
for root, dirs, files in os.walk(r'c:\Users\fena\Desktop\perfulandia\Perfulandia-SPA-FULLSTACK1'):
    if 'target' in dirs:
        dirs.remove('target') # don't visit target directories
    for file in files:
        if file == 'pom.xml':
            update_pom(os.path.join(root, file))

print("Done")
