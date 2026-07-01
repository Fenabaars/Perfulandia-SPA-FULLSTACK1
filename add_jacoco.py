import os

jacoco_plugin = """
			<plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>
				<version>0.8.11</version>
				<executions>
					<execution>
						<goals>
							<goal>prepare-agent</goal>
						</goals>
					</execution>
					<execution>
						<id>report</id>
						<phase>test</phase>
						<goals>
							<goal>report</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
"""

base_dir = r"c:\Users\fena\Desktop\perfulandia\Perfulandia-SPA-FULLSTACK1"

for item in os.listdir(base_dir):
    item_path = os.path.join(base_dir, item)
    if os.path.isdir(item_path) and item.startswith("microservicio-"):
        pom_path = os.path.join(item_path, "pom.xml")
        if os.path.exists(pom_path):
            with open(pom_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if "jacoco-maven-plugin" not in content:
                # Find the last </plugins> to insert before it
                plugins_end_idx = content.rfind("</plugins>")
                if plugins_end_idx != -1:
                    new_content = content[:plugins_end_idx] + jacoco_plugin + content[plugins_end_idx:]
                    with open(pom_path, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    print(f"Added JaCoCo to {item}/pom.xml")
                else:
                    print(f"Could not find </plugins> in {item}/pom.xml")
            else:
                print(f"JaCoCo already in {item}/pom.xml")
