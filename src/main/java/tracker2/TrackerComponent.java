package tracker2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

@Component
public class TrackerComponent {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	
	static final String COMMA = ",";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableA> readFileTableA20120407() throws IOException{
		long startTime = System.currentTimeMillis();
		File file = resourceLoader.getResource("classpath:TableA20120407.csv").getFile();
		List<TableA> tableA = new ArrayList<TableA>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableA.add(new TableA(arr[0].trim(), arr[1].trim(), arr[2].trim(), arr[3].trim(), Long.parseLong(arr[4]), Long.parseLong(arr[5])));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
			System.out.println(String.format("'TableA20120407.csv' file read successfully completed ! time elapsed = %d ms and number of record created = %d", (System.currentTimeMillis() - startTime), tableA.size()));
		}
		return tableA;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableB> readFileTableB20120409() throws IOException{
		long startTime = System.currentTimeMillis();
		File file = resourceLoader.getResource("classpath:TableB20120409.csv").getFile();
		List<TableB> tableB = new ArrayList<TableB>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableB.add(new TableB(arr[0].trim(), arr[1].trim(), arr[2].trim(), arr[3].trim(), Long.parseLong(arr[4]), Long.parseLong(arr[5])));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
			System.out.println(String.format("'TableB20120409.csv' file read successfully completed ! time elapsed = %d ms and number of record created = %d", (System.currentTimeMillis() - startTime), tableB.size()));
		}
		return tableB;
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableC> readFileTableC() throws IOException{
		long startTime = System.currentTimeMillis();
		File file = resourceLoader.getResource("classpath:TableC.csv").getFile();
		List<TableC> tableC = new ArrayList<TableC>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableC.add(new TableC(arr[0], Double.valueOf(arr[1]), Double.valueOf(arr[2]), arr[3], arr[4]));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
			System.out.println(String.format("'TableC.csv' file read successfully completed ! time elapsed = %d ms and number of record created = %d", (System.currentTimeMillis() - startTime), tableC.size()));
		}
		return tableC;
	}
}
